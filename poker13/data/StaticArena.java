package com.chitu.poker.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 战区
 * @author open
 *
 */
public class StaticArena implements BroadcastMessage {

	private int id;
	
	private String name;
	
	private String desc;
	
	private int mixGrade;
	
	private int maxGrade;
	
	private int mixCapability;
	
	private int maxCapability;
	
	private int maxPlayer;
	
	private int strengthLimit;
	
	private int mineId1;
	
	private int mineId2;
	
	private int mineId3;
	
	private int mineId4;
	
	public static StaticArena get(int id) {
		return StaticDataManager.getInstance().get(StaticArena.class, id);
	}
	
	public static Collection<StaticArena> gets(){
		Map<Integer,StaticArena> map = StaticDataManager.getInstance().getMap(StaticArena.class);
		return map.values();
	}
	
	public static StaticArena get(int grade, int capability){
		Map<Integer,StaticArena> map = StaticDataManager.getInstance().getMap(StaticArena.class);
		for(StaticArena data : map.values()){
			if(data.getMixGrade() <= grade && grade <= data.getMaxGrade() &&
					data.getMixCapability() <= capability && capability <= data.getMaxCapability()){
				return data;
			}
		}
		return null;
	}
	
	/**
	 * 战区内的矿
	 * @return
	 */
	public List<StaticArenaMine> mineArenas(){
		List<StaticArenaMine> list = new ArrayList<StaticArenaMine>();
		if(this.mineId1 > 0){
			list.add(StaticArenaMine.get(this.mineId1));
		}
		if(this.mineId2 > 0){
			list.add(StaticArenaMine.get(this.mineId2));
		}
		if(this.mineId3 > 0){
			list.add(StaticArenaMine.get(this.mineId3));
		}
		if(this.mineId4 > 0){
			list.add(StaticArenaMine.get(this.mineId4));
		}
		return list;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**要求最低等级**/
	public int getMixGrade() {
		return mixGrade;
	}

	public void setMixGrade(int mixGrade) {
		this.mixGrade = mixGrade;
	}

	/**要求最高等级**/
	public int getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}

	/**要求最小战力**/
	public int getMixCapability() {
		return mixCapability;
	}

	public void setMixCapability(int mixCapability) {
		this.mixCapability = mixCapability;
	}

	/**要求最大战力**/
	public int getMaxCapability() {
		return maxCapability;
	}

	public void setMaxCapability(int maxCapability) {
		this.maxCapability = maxCapability;
	}
	
	/**占矿人数上限,最低排名**/
	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	/**矿1**/
	public int getMineId1() {
		return mineId1;
	}

	public void setMineId1(int mineId1) {
		this.mineId1 = mineId1;
	}

	/**矿2**/
	public int getMineId2() {
		return mineId2;
	}

	public void setMineId2(int mineId2) {
		this.mineId2 = mineId2;
	}

	/**矿3**/
	public int getMineId3() {
		return mineId3;
	}

	public void setMineId3(int mineId3) {
		this.mineId3 = mineId3;
	}

	/**矿4**/
	public int getMineId4() {
		return mineId4;
	}

	public void setMineId4(int mineId4) {
		this.mineId4 = mineId4;
	}

	/**体力要求**/
	public int getStrengthLimit() {
		return strengthLimit;
	}

	public void setStrengthLimit(int strengthLimit) {
		this.strengthLimit = strengthLimit;
	}
	
	
	
}
