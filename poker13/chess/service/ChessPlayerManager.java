package com.chitu.chess.service;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import cn.gecko.broadcast.RequestUtils;
import cn.gecko.broadcast.api.ConnectorManager;
import cn.gecko.commons.event.HeartBeatEvent;
import cn.gecko.commons.event.HeartBeatEventListener;
import cn.gecko.player.service.PlayerManager;

import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.model.ChessUtils;
import com.chitu.chess.model.OffChessPlayer;

@Service
public class ChessPlayerManager extends PlayerManager<ChessPlayer> implements HeartBeatEventListener {
	private Map<Long, ChessPlayer> offPlayerMap = new ConcurrentHashMap<Long, ChessPlayer>();
	private Map<Long, Integer> registry = new TreeMap<Long, Integer>();

	public long heartbeatpoint = System.currentTimeMillis();
	public long launchpoint = System.currentTimeMillis();
	/**
	 * 用户信息，无论在不在线，在线返回ChessPlayer，不在线返回OffChessPlayer
	 * 
	 * @param playerId
	 * @return
	 */
	public ChessPlayer getAnyPlayerById(long playerId) {
		ChessPlayer player = getOnlinePlayerById(playerId);
		if (player == null) {
			player = offPlayerMap.get(playerId);
			if (player == null) {
				player = new OffChessPlayer(playerId);
				offPlayerMap.put(playerId, player);
			}
		}
		return player;
	}

	public ChessPlayer removeOffPlayer(long playerId) {
		return offPlayerMap.remove(playerId);
	}

	public ChessPlayer getRequestPlayer() {
		return super.getPlayer(RequestUtils.getCurrentSid());
	}

	@Override
	protected ChessPlayer createPlayer(int sid, ConnectorManager connectorManager) {
		return new ChessPlayer(sid, connectorManager);
	}

	@Override
	public void onEvent(HeartBeatEvent event) {
		Collection<ChessPlayer> players = getOnlinePlayers();
		heartbeatpoint = System.currentTimeMillis();
		for (ChessPlayer player : players) {
			player.heartbeat();
		}
	}

	public void persistAll() {
		Collection<ChessPlayer> players = getOnlinePlayers();
		for (ChessPlayer player : players) {
			player.persistAll();
		}
		players = offPlayerMap.values();
		for (ChessPlayer player : players) {
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
		Collection<ChessPlayer> players = offPlayerMap.values();
		for (ChessPlayer player : players) {
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
	
	public Map<Long, Integer> getRegistry() {
		return registry;
	}

	public synchronized void setRegistry(Long now) {
		Long delta = (now - launchpoint)/86400000;
		
		if(registry.containsKey(delta)){
			registry.put(delta, registry.get(delta)+1);
		}else{
			registry.put(delta, 1);
		}
	}

}
