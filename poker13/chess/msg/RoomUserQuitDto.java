package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;

public class RoomUserQuitDto extends GeneralResponse implements BroadcastMessage {
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
