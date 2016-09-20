package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;

public class RoomUserReadyReturnDto extends GeneralResponse {

	public RoomUserReadyReturnDto(int ok){
		this.ok = ok;
	}
	
	/**
	 * 准备返回 0是失败 1是成功
	 */
	private int ok;

	/**
	 * 准备返回 0是失败 1是成功
	 */
	public int getOk() {
		return ok;
	}

	public void setOk(int ok) {
		this.ok = ok;
	}

	
}
