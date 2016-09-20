package com.chitu.poker.data;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 关卡数据
 * 
 * @author ivan
 * 
 */
public class StaticBattle implements BroadcastMessage {

	public enum PosType {
		/**
		 * 怪物
		 */
		Monster,
		/**
		 * 入口
		 */
		Entrance,
		/**
		 * 出口
		 */
		Exit,
		/**
		 * 宝箱
		 */
		Box
	}

	private int id;

	private String name;

	private int instanceId;

	private int bgId;

	private int posType;

	private int landType;

	private int x;

	private int y;

	private boolean rewardBox;

	private String pathPoints;

	private int preBattleId;

	private boolean finalBattle;

	private int monsterId;

	private int needStrength;

	private int rewardExp;

	private int rewardMinMoney;

	private int rewardMaxMoney;

	private int rewardPetId1;

	private int rewardPetRate1;

	private int rewardPetId2;

	private int rewardPetRate2;

	private int rewardPetId3;

	private int rewardPetRate3;

	private int rewardPetId4;

	private int rewardPetRate4;

	private int rewardPetId5;

	private int rewardPetRate5;

	public static StaticBattle get(int id) {
		return StaticDataManager.getInstance().get(StaticBattle.class, id);
	}

	/**
	 * ID
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 关卡名字
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 所属副本ID
	 * 
	 * @return
	 */
	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * 战斗背景ID
	 * 
	 * @return
	 */
	public int getBgId() {
		return bgId;
	}

	public void setBgId(int bgId) {
		this.bgId = bgId;
	}

	/**
	 * 关卡点类型，参考PosType系列常量
	 * 
	 * @return
	 */
	public int getPosType() {
		return posType;
	}

	public void setPosType(int posType) {
		this.posType = posType;
	}

	/**
	 * 地形类型，参考StaticInstance.LandType系列常量
	 * 
	 * @return
	 */
	public int getLandType() {
		return landType;
	}

	public void setLandType(int landType) {
		this.landType = landType;
	}

	/**
	 * X坐标
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Y坐标
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 是否有宝箱
	 * 
	 * @return
	 */
	public boolean isRewardBox() {
		return rewardBox;
	}

	public void setRewardBox(boolean rewardBox) {
		this.rewardBox = rewardBox;
	}

	/**
	 * 从前置关卡到本关卡经过的点，格式：x1,y1;x2,y2
	 * 
	 * @return
	 */
	public String getPathPoints() {
		return pathPoints;
	}

	public void setPathPoints(String pathPoints) {
		this.pathPoints = pathPoints;
	}

	/**
	 * 前置关卡ID，-1为入口，-2为出口，0为起始关卡，最终关卡由finalBattle属性决定
	 * 
	 * @return
	 */
	public int getPreBattleId() {
		return preBattleId;
	}

	public void setPreBattleId(int preBattleId) {
		this.preBattleId = preBattleId;
	}

	/**
	 * 是否最终关卡
	 * 
	 * @return
	 */
	public boolean isFinalBattle() {
		return finalBattle;
	}

	public void setFinalBattle(boolean finalBattle) {
		this.finalBattle = finalBattle;
	}

	/**
	 * 怪物ID
	 * 
	 * @return
	 */
	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	/**
	 * 进入副本需要的体力
	 * 
	 * @return
	 */
	public int getNeedStrength() {
		return needStrength;
	}

	public void setNeedStrength(int needStrength) {
		this.needStrength = needStrength;
	}

	/**
	 * 奖励经验
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
	 * 奖励的最少金币
	 * 
	 * @return
	 */
	public int getRewardMinMoney() {
		return rewardMinMoney;
	}

	public void setRewardMinMoney(int rewardMinMoney) {
		this.rewardMinMoney = rewardMinMoney;
	}

	/**
	 * 奖励的最多金币
	 * 
	 * @return
	 */
	public int getRewardMaxMoney() {
		return rewardMaxMoney;
	}

	public void setRewardMaxMoney(int rewardMaxMoney) {
		this.rewardMaxMoney = rewardMaxMoney;
	}

	/**
	 * 奖励宠物ID1
	 * 
	 * @return
	 */
	public int getRewardPetId1() {
		return rewardPetId1;
	}

	public void setRewardPetId1(int rewardPetId1) {
		this.rewardPetId1 = rewardPetId1;
	}

	/**
	 * 奖励宠物几率1，万分比
	 * 
	 * @return
	 */
	public int getRewardPetRate1() {
		return rewardPetRate1;
	}

	public void setRewardPetRate1(int rewardPetRate1) {
		this.rewardPetRate1 = rewardPetRate1;
	}

	/**
	 * 奖励宠物ID2
	 * 
	 * @return
	 */
	public int getRewardPetId2() {
		return rewardPetId2;
	}

	public void setRewardPetId2(int rewardPetId2) {
		this.rewardPetId2 = rewardPetId2;
	}

	/**
	 * 奖励宠物几率2，万分比
	 * 
	 * @return
	 */
	public int getRewardPetRate2() {
		return rewardPetRate2;
	}

	public void setRewardPetRate2(int rewardPetRate2) {
		this.rewardPetRate2 = rewardPetRate2;
	}

	/**
	 * 奖励宠物ID3
	 * 
	 * @return
	 */
	public int getRewardPetId3() {
		return rewardPetId3;
	}

	public void setRewardPetId3(int rewardPetId3) {
		this.rewardPetId3 = rewardPetId3;
	}

	/**
	 * 奖励宠物几率3，万分比
	 * 
	 * @return
	 */
	public int getRewardPetRate3() {
		return rewardPetRate3;
	}

	public void setRewardPetRate3(int rewardPetRate3) {
		this.rewardPetRate3 = rewardPetRate3;
	}

	/**
	 * 奖励宠物ID4
	 * 
	 * @return
	 */
	public int getRewardPetId4() {
		return rewardPetId4;
	}

	public void setRewardPetId4(int rewardPetId4) {
		this.rewardPetId4 = rewardPetId4;
	}

	/**
	 * 奖励宠物几率4，万分比
	 * 
	 * @return
	 */
	public int getRewardPetRate4() {
		return rewardPetRate4;
	}

	public void setRewardPetRate4(int rewardPetRate4) {
		this.rewardPetRate4 = rewardPetRate4;
	}

	/**
	 * 奖励宠物ID5
	 * 
	 * @return
	 */
	public int getRewardPetId5() {
		return rewardPetId5;
	}

	public void setRewardPetId5(int rewardPetId5) {
		this.rewardPetId5 = rewardPetId5;
	}

	/**
	 * 奖励宠物几率5，万分比
	 * 
	 * @return
	 */
	public int getRewardPetRate5() {
		return rewardPetRate5;
	}

	public void setRewardPetRate5(int rewardPetRate5) {
		this.rewardPetRate5 = rewardPetRate5;
	}

}
