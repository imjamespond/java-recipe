package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.GeneralResponse;

import com.chitu.poker.battle.SaveRecord;
import com.chitu.poker.data.StaticInstance;

/**
 * 副本累计奖励
 * 
 * @author ivan
 * 
 */
public class InstanceRewardResult extends GeneralResponse {

	private String instanceName;

	private int rewardExp;

	private int rewardMoney;

	private int[] rewardPets;
	
	public InstanceRewardResult(String instanceName) {
		this.instanceName=instanceName;
	}

	public InstanceRewardResult(SaveRecord record) {
		StaticInstance instance = StaticInstance.get(record.instanceId);
		this.instanceName = instance.getName();
		this.rewardExp = record.rewardExp;
		this.rewardMoney = record.rewardMoney;
		this.rewardPets = record.rewardPetIds;
	}

	/**
	 * 副本名字
	 * 
	 * @return
	 */
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * 累计奖励的经验
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
	 * 累计奖励的金币
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
	 * 累计奖励的宠物，int数组，宠物的静态数据
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
