package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 放牌信息
 * 
 * @author ivan
 * 
 */
public class PutPokerResult implements BroadcastMessage {

	private int tableIndex1;

	private int pokerIndex1;

	private int tableIndex2;

	private int pokerIndex2;

	public PutPokerResult(int tableIndex1, int pokerIndex1, int tableIndex2, int pokerIndex2) {
		this.tableIndex1 = tableIndex1;
		this.pokerIndex1 = pokerIndex1;
		this.tableIndex2 = tableIndex2;
		this.pokerIndex2 = pokerIndex2;
	}

	/**
	 * 第一张牌在台上的位置
	 * 
	 * @return
	 */
	public int getTableIndex1() {
		return tableIndex1;
	}

	public void setTableIndex1(int tableIndex1) {
		this.tableIndex1 = tableIndex1;
	}

	/**
	 * 第一张牌在出牌方手上牌的位置
	 * 
	 * @return
	 */
	public int getPokerIndex1() {
		return pokerIndex1;
	}

	public void setPokerIndex1(int pokerIndex1) {
		this.pokerIndex1 = pokerIndex1;
	}

	/**
	 * 第二张牌在台上的位置
	 * 
	 * @return
	 */
	public int getTableIndex2() {
		return tableIndex2;
	}

	public void setTableIndex2(int tableIndex2) {
		this.tableIndex2 = tableIndex2;
	}

	/**
	 * 第二张牌在出牌方手上牌的位置
	 * 
	 * @return
	 */
	public int getPokerIndex2() {
		return pokerIndex2;
	}

	public void setPokerIndex2(int pokerIndex2) {
		this.pokerIndex2 = pokerIndex2;
	}

}
