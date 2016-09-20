package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;

public class RoomUserMatchListDto extends GeneralResponse {

	private RoomUserMatchDto[] roomUserMatchDto;

	public RoomUserMatchListDto() {

	}

	/**RoomUserMatchDto[] 比赛列表
	 * @return
	 */
	public RoomUserMatchDto[] getRoomUserMatchDto() {
		return roomUserMatchDto;
	}

	public void setRoomUserMatchDto(RoomUserMatchDto[] roomUserMatchDto) {
		this.roomUserMatchDto = roomUserMatchDto;
	}
	
}
