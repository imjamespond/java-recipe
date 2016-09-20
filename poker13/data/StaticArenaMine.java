package com.chitu.poker.data;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 战区矿位
 * @author open
 *
 */
public class StaticArenaMine implements BroadcastMessage{

	private int id;
	
	private int arenaId;
	
	private String name;
	
	private String desc;
	
	private int level;
	
	private int outPutTime;
	
	private int outPutMoney;
	
	private int mixRank;
	
	private int maxRank;
	
	
	public static StaticArenaMine get(int id) {
		return StaticDataManager.getInstance().get(StaticArenaMine.class, id);
	}

    /**ID**/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    /**名称**/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    /**说明**/
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

    /**级别**/
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

    /**产出时间CD,豪秒**/
	public int getOutPutTime() {
		return outPutTime;
	}

	public void setOutPutTime(int outPutTime) {
		this.outPutTime = outPutTime;
	}

    /**产出金币**/
	public int getOutPutMoney() {
		return outPutMoney;
	}

	public void setOutPutMoney(int outPutMoney) {
		this.outPutMoney = outPutMoney;
	}

    /**要求最低排名**/
	public int getMixRank() {
		return mixRank;
	}

	public void setMixRank(int mixRank) {
		this.mixRank = mixRank;
	}

    /**要求最高排名**/
	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	/**所属战区**/
	public int getArenaId() {
		return arenaId;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}
	
	
	
	
	
	
	
}
