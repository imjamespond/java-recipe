package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;

//not used for now
public class ChessSpeakerDto implements
		BroadcastMessage {

	public ChessSpeakerDto(){
	}
	
	/**
	 * msg
	 */
	private String msg;
	/**
	 * 次数
	 */
	private int count;

	/**
	 * 消息
	 */
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 次数
	 */
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
