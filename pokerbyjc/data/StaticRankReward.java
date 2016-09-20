package com.chitu.poker.data;

import java.util.Map;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 排名奖励
 * @author open
 *
 */
public class StaticRankReward implements BroadcastMessage{

	private int id;
	
	private int mixRank;
	
	private int maxRank;
	
	private int money;
	
	private int point;
	
	private int staticPet1;
	
	private int petCount1;
	
    private int staticPet2;
	
	private int petCount2;
	
    private int staticPet3;
	
	private int petCount3;
	
    private int staticPet4;
	
	private int petCount4;
	
	public static StaticRankReward get(int id) {
		return StaticDataManager.getInstance().get(StaticRankReward.class, id);
	}
	
	public static StaticRankReward getByRank(int rank){
		Map<Integer,StaticRankReward> map = StaticDataManager.getInstance().getMap(StaticRankReward.class);
		for(StaticRankReward data : map.values()){
			if(data.getMixRank() <= rank && rank <= data.getMaxRank()){
				return data;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMixRank() {
		return mixRank;
	}

	public void setMixRank(int mixRank) {
		this.mixRank = mixRank;
	}

	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getStaticPet1() {
		return staticPet1;
	}

	public void setStaticPet1(int staticPet1) {
		this.staticPet1 = staticPet1;
	}

	public int getPetCount1() {
		return petCount1;
	}

	public void setPetCount1(int petCount1) {
		this.petCount1 = petCount1;
	}

	public int getStaticPet2() {
		return staticPet2;
	}

	public void setStaticPet2(int staticPet2) {
		this.staticPet2 = staticPet2;
	}

	public int getPetCount2() {
		return petCount2;
	}

	public void setPetCount2(int petCount2) {
		this.petCount2 = petCount2;
	}

	public int getStaticPet3() {
		return staticPet3;
	}

	public void setStaticPet3(int staticPet3) {
		this.staticPet3 = staticPet3;
	}

	public int getPetCount3() {
		return petCount3;
	}

	public void setPetCount3(int petCount3) {
		this.petCount3 = petCount3;
	}

	public int getStaticPet4() {
		return staticPet4;
	}

	public void setStaticPet4(int staticPet4) {
		this.staticPet4 = staticPet4;
	}

	public int getPetCount4() {
		return petCount4;
	}

	public void setPetCount4(int petCount4) {
		this.petCount4 = petCount4;
	}
	
	
}
