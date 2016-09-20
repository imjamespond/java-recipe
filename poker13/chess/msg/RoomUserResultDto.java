package com.chitu.chess.msg;

import java.util.List;


import cn.gecko.broadcast.BroadcastMessage;

public class RoomUserResultDto implements BroadcastMessage {

	private RoomUserShowCardDto[] roomUserCardDto;

	public RoomUserResultDto(List<RoomUserShowCardDto> liRoomUserCardDto) {

		roomUserCardDto = new RoomUserShowCardDto[liRoomUserCardDto.size()];
		liRoomUserCardDto.toArray(roomUserCardDto);
	}

	/**RoomUserCardDto[] 玩家出牌数组
	 * @return
	 */
	public RoomUserShowCardDto[] getRoomUserCardDto() {
		return roomUserCardDto;
	}

	public void setRoomUserCardDto(RoomUserShowCardDto[] roomUserCardDto) {
		this.roomUserCardDto = roomUserCardDto;
	}
	
}
