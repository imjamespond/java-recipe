package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;

public class RoomUserCardDto extends GeneralResponse implements BroadcastMessage {
	
	public RoomUserCardDto(int[] playerCard){
		this.playerCard = playerCard;
	}
	
	/**
	 * 玩家拿牌
	 */
	private int[] playerCard;


	
	/**
	 * 玩家拿牌int[] 0为方块2 1为梅花2 2为红桃2 3为黑桃2 ... 51为黑桃A
	 */
	public int[] getPlayerCard() {
		return playerCard;
	}

	public void setPlayerCard(int[] playerCard) {
		this.playerCard = playerCard;
	}
	
}
