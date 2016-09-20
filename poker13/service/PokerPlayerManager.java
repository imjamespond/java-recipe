package com.chitu.poker.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import cn.gecko.broadcast.RequestUtils;
import cn.gecko.broadcast.api.ConnectorManager;
import cn.gecko.commons.event.HeartBeatEvent;
import cn.gecko.commons.event.HeartBeatEventListener;
import cn.gecko.player.service.PlayerManager;

import com.chitu.poker.model.OffPokerPlayer;
import com.chitu.poker.model.PokerPlayer;

@Service
public class PokerPlayerManager extends PlayerManager<PokerPlayer> implements HeartBeatEventListener {
	
	private Map<Long, PokerPlayer> offPlayerMap = new ConcurrentHashMap<Long, PokerPlayer>();

	public long heartbeatpoint = System.currentTimeMillis();

	
	/**
	 * 用户信息，无论在不在线，在线返回PokerPlayer，不在线返回OffPokerPlayer
	 * 
	 * @param playerId
	 * @return
	 */
	public PokerPlayer getAnyPlayerById(long playerId) {
		if(playerId <= 0){
			return null;
		}
		
		PokerPlayer player = getOnlinePlayerById(playerId);
		if (player == null) {
			player = offPlayerMap.get(playerId);
			if (player == null) {
				player = new OffPokerPlayer(playerId);
				offPlayerMap.put(playerId, player);
			}
		}
		return player;
	}
	
	public PokerPlayer removeOffPlayer(long playerId) {
		return offPlayerMap.remove(playerId);
	}

	public PokerPlayer getRequestPlayer() {
		return super.getPlayer(RequestUtils.getCurrentSid());
	}

	@Override
	protected PokerPlayer createPlayer(int sid, ConnectorManager connectorManager) {
		return new PokerPlayer(sid, connectorManager);
	}
	
	@Override
	public void onEvent(HeartBeatEvent event) {
		Collection<PokerPlayer> players = getOnlinePlayers();
		heartbeatpoint = System.currentTimeMillis();
		for (PokerPlayer player : players) {
			player.heartbeat();
		}
		
		for(PokerPlayer player : offPlayerMap.values()){
			player.heartbeat();
		}
	}

	public void persistAll() {
		Collection<PokerPlayer> players = getOnlinePlayers();
		for (PokerPlayer player : players) {
			player.persistAll();
		}
		players = offPlayerMap.values();
		for (PokerPlayer player : players) {
			if (this.containPlayer(player.id)) {
				offPlayerMap.remove(player.id);
				continue;
			}
			player.persistAll();
		}
	}

	@PreDestroy
	public void destroy() {
		super.destroy();
		Collection<PokerPlayer> players = offPlayerMap.values();
		for (PokerPlayer player : players) {
			if (this.containPlayer(player.id)) {
				offPlayerMap.remove(player.id);
				continue;
			}
			player.persistAll();
		}
	}

	public int getOfflineCount() {
		return offPlayerMap.size();
	}
}
