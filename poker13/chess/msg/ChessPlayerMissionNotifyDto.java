package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;


/**
 * 任务完成通知
 * @author Administrator
 *
 */

public class ChessPlayerMissionNotifyDto implements BroadcastMessage {


	private boolean missionDone = false;
	
	public ChessPlayerMissionNotifyDto(boolean missionDone) {
		this.missionDone = missionDone;
	}
	
	/**有任务完成
	 * @return
	 */
	public boolean getMissionDone() {
		return missionDone;
	}

	public void setMissionDone(boolean missionDone) {
		this.missionDone = missionDone;
	}
}
