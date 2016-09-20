package com.chitu.poker.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.model.PokerPlayer;

/**
 * 战力变更通知
 * @author open
 *
 */
public class CapabilityNotify implements BroadcastMessage {

	private String playerId;
	
	private int capability;
	
	public CapabilityNotify(PokerPlayer player){
		this.playerId = String.valueOf(player.id);
		this.capability = player.capability;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**总战力**/
	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}
	
	
}
