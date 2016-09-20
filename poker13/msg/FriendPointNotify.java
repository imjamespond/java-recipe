package com.chitu.poker.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.model.PokerPlayer;

/**
 * 友情点变更通知
 * 
 * @author open
 * 
 */
public class FriendPointNotify implements BroadcastMessage {

	private String playerId;

	private int friendPoint;

	public FriendPointNotify(PokerPlayer player) {
		this.playerId = String.valueOf(player.id);
		this.friendPoint = player.friendPoint;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * 当前友情点
	 * 
	 * @return
	 */
	public int getFriendPoint() {
		return friendPoint;
	}

	public void setFriendPoint(int friendPoint) {
		this.friendPoint = friendPoint;
	}

}
