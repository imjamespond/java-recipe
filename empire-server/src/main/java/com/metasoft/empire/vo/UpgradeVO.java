package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;

@DescAnno("升级消息")
public class UpgradeVO {
	private int roleid;
	private int number;
	private int upgrade;
	private int level;
	private int attack;
	private int hp;
	
	public UpgradeVO(int roleid, int number, int upgrade, int level,
			int attack, int hp) {
		super();
		this.roleid = roleid;
		this.number = number;
		this.upgrade = upgrade;
		this.level = level;
		this.attack = attack;
		this.hp = hp;
	}
	public UpgradeVO() {
		super();
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}

}
