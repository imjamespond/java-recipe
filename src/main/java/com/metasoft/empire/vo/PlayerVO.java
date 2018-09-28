package com.metasoft.empire.vo;

import com.metasoft.empire.utils.RandomUtils;

public class PlayerVO {
	private long uid;
	private String name;
	private int hp;
	private int defense;
	private int attack;
	private int level;
	private int type;
	private int[] roles = new int[4];
	private int[] swap = {1,2,3,4,5,6,7,8};
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public void decreaseHp(int hp) {
		this.hp = this.hp>hp?this.hp-hp:0;
	}
	public void increaseHp(int hp, int maxhp) {
		this.hp += hp;
		this.hp = this.hp> maxhp?maxhp:this.hp;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int[] getRoles() {
		return roles;
	}
	public void setRoles(int[] roles) {
		this.roles = roles;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int[] getSwap() {
		return swap;
	}
	public void setSwap(int[] swap) {
		this.swap = swap;
	}
	public void randomSwap(){
		int counterNum = swap.length;
		while (--counterNum >= 0) {
			int index = RandomUtils.nextInt(0,counterNum);
			int tmp = swap[index];
			swap[index] = swap[counterNum];
			swap[counterNum] = tmp;
		}
	}
	
}
