package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;

public class RoomUserMatchFinishDto  extends GeneralResponse {

	public int rank;
	public int state;

	public RoomUserMatchFinishDto() {

	}
	/**
	 * 比赛排名
	 * @return
	 */

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * 比赛状态 1为没淘汰 0为淘汰
	 * @return
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	
}
