package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.GeneralResponse;

/**
 * 换王结果
 * 
 * @author ivan
 * 
 */
public class ChangeJokerResult extends GeneralResponse {

	private PokerDto newPoker;

	private int restPower;

	public ChangeJokerResult(PokerDto newPoker, int restPower) {
		this.newPoker = newPoker;
		this.restPower = restPower;
	}

	/**
	 * 换回来的牌
	 * 
	 * @return
	 */
	public PokerDto getNewPoker() {
		return newPoker;
	}

	public void setNewPoker(PokerDto newPoker) {
		this.newPoker = newPoker;
	}

	/**
	 * 剩余能量点
	 * 
	 * @return
	 */
	public int getRestPower() {
		return restPower;
	}

	public void setRestPower(int restPower) {
		this.restPower = restPower;
	}

}
