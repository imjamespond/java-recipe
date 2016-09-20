package com.chitu.poker.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.model.PokerPlayer;

/**
 * 友情点变更通知
 * 
 * @author open
 * 
 */
public class StrengthNotify implements BroadcastMessage {

	private String playerId;

	private int strength;

	public StrengthNotify(PokerPlayer player) {
		this.playerId = String.valueOf(player.id);
		this.strength = player.strength;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * 当前体力值
	 * 
	 * @return
	 */
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}
