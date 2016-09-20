package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;


/**
 * @author Administrator
 *
 */
public class ChessPlayerMissionPrizeDto extends GeneralResponse {


	private int playerMoney;
	private int money;
	private int next;
	private boolean missionDone = false;
	

	public ChessPlayerMissionPrizeDto() {
		
	}



	/**
	 * 玩家金币
	 * @return
	 */
	public int getPlayerMoney() {
		return playerMoney;
	}



	public void setPlayerMoney(int playerMoney) {
		this.playerMoney = playerMoney;
	}



	/**
	 * 奖励金币
	 * @return
	 */
	public int getMoney() {
		return money;
	}



	public void setMoney(int money) {
		this.money = money;
	}



	/**
	 * 下个任务的id
	 * @return
	 */
	public int getNext() {
		return next;
	}



	public void setNext(int next) {
		this.next = next;
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
