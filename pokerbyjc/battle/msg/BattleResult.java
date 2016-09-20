package com.chitu.poker.battle.msg;

import java.util.List;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 战斗结果
 * 
 * @author ivan
 * 
 */
public class BattleResult implements BroadcastMessage {

	private int battleId;

	private List<BattleLineResult> lineResults;

	private boolean win;

	private int rewardExp;

	private int rewardMoney;

	private int[] rewardPets;

	public BattleResult(int battleId, List<BattleLineResult> lineResults, boolean win) {
		this.battleId = battleId;
		this.lineResults = lineResults;
		this.win = win;
	}

	/**
	 * 关卡ID
	 * 
	 * @return
	 */
	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	/**
	 * 所有牌型线的战斗结果，BattleLineResult数组
	 * 
	 * @return
	 */
	public List<BattleLineResult> getLineResults() {
		return lineResults;
	}

	public void setLineResults(List<BattleLineResult> lineResults) {
		this.lineResults = lineResults;
	}

	/**
	 * 是否胜利
	 * 
	 * @return
	 */
	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	/**
	 * 奖励的经验
	 * 
	 * @return
	 */
	public int getRewardExp() {
		return rewardExp;
	}

	public void setRewardExp(int rewardExp) {
		this.rewardExp = rewardExp;
	}

	/**
	 * 奖励的金币
	 * 
	 * @return
	 */
	public int getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(int rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	/**
	 * 奖励的宠物，int数组，宠物的静态数据
	 * 
	 * @return
	 */
	public int[] getRewardPets() {
		return rewardPets;
	}

	public void setRewardPets(int[] rewardPets) {
		this.rewardPets = rewardPets;
	}

}
