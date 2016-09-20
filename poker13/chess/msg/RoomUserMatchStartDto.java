package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;

public class RoomUserMatchStartDto implements BroadcastMessage {

	public ChessRoomDto chessRoom;
	public RoomUserCardDto userCard;

	public RoomUserMatchStartDto() {

	}

	
	/**
	 * 房间信息
	 * @return
	 */
	public ChessRoomDto getChessRoom() {
		return chessRoom;
	}

	public void setChessRoom(ChessRoomDto chessRoom) {
		this.chessRoom = chessRoom;
	}

	/**
	 * 玩家的牌
	 * @return
	 */
	public RoomUserCardDto getUserCard() {
		return userCard;
	}

	public void setUserCard(RoomUserCardDto userCard) {
		this.userCard = userCard;
	}




	
}
