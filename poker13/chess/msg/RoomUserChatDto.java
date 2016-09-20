package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;

/*
 * 房间内玩家
 */
public class RoomUserChatDto extends GeneralResponse implements BroadcastMessage {

	
	private String playerId;	
	private String playerName;
	
	private int position;	
	private int type;
	/**
	 * 玩家id
	 */
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * 玩家NAME
	 */
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**位置**/
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * 聊天语句int型
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

	
	
	
}
