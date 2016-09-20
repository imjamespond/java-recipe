package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;

public class RoomUserChangeDto implements
		BroadcastMessage {

	public RoomUserChangeDto(long playerId){
		this.playerId = String.valueOf(playerId);
	}
	
	/**
	 * 玩家id
	 */
	private String playerId;
	/**
	 * 玩家id
	 */
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
}
