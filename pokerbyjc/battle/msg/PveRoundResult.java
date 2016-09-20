package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.GeneralResponse;

/**
 * pve战斗一个回合的所有数据
 * 
 * @author ivan
 * 
 */
public class PveRoundResult extends GeneralResponse {

	private BattleResult myResult;

	private PokerResetResult myReset;

	private SendPokerResult antiSendPoker;

	private PutPokerResult antiPutPoker;

	private BattleResult antiResult;

	private PokerResetResult antiReset;

	private int nextRoundIndex;

	private SendPokerResult mySendPoker;

	/**
	 * 我方攻击的效果，pass的话为null
	 * 
	 * @return
	 */
	public BattleResult getMyResult() {
		return myResult;
	}

	public void setMyResult(BattleResult myResult) {
		this.myResult = myResult;
	}

	/**
	 * 我方攻击后引起的重置效果，台面满牌会引起重置，没有重置为null
	 * 
	 * @return
	 */
	public PokerResetResult getMyReset() {
		return myReset;
	}

	public void setMyReset(PokerResetResult myReset) {
		this.myReset = myReset;
	}

	/**
	 * 对方补牌结果
	 * 
	 * @return
	 */
	public SendPokerResult getAntiSendPoker() {
		return antiSendPoker;
	}

	public void setAntiSendPoker(SendPokerResult antiSendPoker) {
		this.antiSendPoker = antiSendPoker;
	}

	/**
	 * 对方放牌数据，pass为null
	 * 
	 * @return
	 */
	public PutPokerResult getAntiPutPoker() {
		return antiPutPoker;
	}

	public void setAntiPutPoker(PutPokerResult antiPutPoker) {
		this.antiPutPoker = antiPutPoker;
	}

	/**
	 * 对方攻击的效果，pass为null
	 * 
	 * @return
	 */
	public BattleResult getAntiResult() {
		return antiResult;
	}

	public void setAntiResult(BattleResult antiResult) {
		this.antiResult = antiResult;
	}

	/**
	 * 对方攻击后的重置效果，台面满牌会引起重置，没有重置为null
	 * 
	 * @return
	 */
	public PokerResetResult getAntiReset() {
		return antiReset;
	}

	public void setAntiReset(PokerResetResult antiReset) {
		this.antiReset = antiReset;
	}

	/**
	 * 下一回合数
	 * 
	 * @return
	 */
	public int getNextRoundIndex() {
		return nextRoundIndex;
	}

	public void setNextRoundIndex(int nextRoundIndex) {
		this.nextRoundIndex = nextRoundIndex;
	}

	/**
	 * 给我方补牌结果
	 * 
	 * @return
	 */
	public SendPokerResult getMySendPoker() {
		return mySendPoker;
	}

	public void setMySendPoker(SendPokerResult mySendPoker) {
		this.mySendPoker = mySendPoker;
	}

}
