package com.chitu.chess.msg;

import java.util.List;

import cn.gecko.broadcast.GeneralResponse;

public class RoomUserResultReplayDto extends GeneralResponse {

	public List<RoomUserShowCardReplayDto> roomUserCardDto;

	public RoomUserResultReplayDto(List<RoomUserShowCardReplayDto> liRoomUserCardDto) {
		this.roomUserCardDto = liRoomUserCardDto;
	}

	/**玩家出牌数组,元素RoomUserShowCardReplayDto**/
	public List<RoomUserShowCardReplayDto> getRoomUserCardDto() {
		return roomUserCardDto;
	}

	public void setRoomUserCardDto(List<RoomUserShowCardReplayDto> roomUserCardDto) {
		this.roomUserCardDto = roomUserCardDto;
	}

}
