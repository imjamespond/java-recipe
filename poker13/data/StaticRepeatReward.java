package com.chitu.poker.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 连胜奖励
 * @author open
 *
 */
public class StaticRepeatReward implements BroadcastMessage{

	private int id;
	
	private int repeat;
	
    private int money;
	
	private int point;
	
	private int staticPet1;
	
	private int petCount1;
	
    private int staticPet2;
	
	private int petCount2;
	
	public static StaticRepeatReward get(int id) {
		return StaticDataManager.getInstance().get(StaticRepeatReward.class, id);
	}
	
	public static List<StaticRepeatReward> getByRepeat(int repeat){
		List<StaticRepeatReward> list = new ArrayList<StaticRepeatReward>();
		Map<Integer,StaticRepeatReward> map = StaticDataManager.getInstance().getMap(StaticRepeatReward.class);
		for(StaticRepeatReward data : map.values()){
			if(data.getRepeat() <= repeat){
				list.add(data);
			}
		}
		return list;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
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
	
	
	
}
