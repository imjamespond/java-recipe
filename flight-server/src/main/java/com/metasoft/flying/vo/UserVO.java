package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("用户信息")
public class UserVO {

	@DescAnno("id")
	private long userId;
	@DescAnno("昵称")
	private String name;
	@DescAnno("玫瑰")
	private int rose;
	@DescAnno("金币")
	private int gold;
	@DescAnno("小苹果")
	private int apple;
	@DescAnno("分数")
	private int score;
	@DescAnno("贡献")
	private int contribute;
	@DescAnno("魅力")
	private int charm;
	@DescAnno("性别1男2女")
	private int gender;
	@DescAnno("新手指引")
	private long guide;
	@DescAnno("pve")
	private int pve;
	@DescAnno("pve次数")
	private int pveNum;
	@DescAnno("比赛次数")
	private int matchNum;
	@DescAnno("pve升级")
	private int[] pveUpgrade;
	@DescAnno("组名")
	private String group;
	@DescAnno("头像")
	private String avatar;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}



	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getApple() {
		return apple;
	}

	public void setApple(int apple) {
		this.apple = apple;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getContribute() {
		return contribute;
	}

	public void setContribute(int contribute) {
		this.contribute = contribute;
	}

	public int getCharm() {
		return charm;
	}

	public void setCharm(int charm) {
		this.charm = charm;
	}

	public long getGuide() {
		return guide;
	}

	public void setGuide(long guide) {
		this.guide = guide;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getPve() {
		return pve;
	}

	public void setPve(int pve) {
		this.pve = pve;
	}

	public int getPveNum() {
		return pveNum;
	}

	public void setPveNum(int pveNum) {
		this.pveNum = pveNum;
	}

	public int[] getPveUpgrade() {
		return pveUpgrade;
	}

	public void setPveUpgrade(int[] pveUpgrade) {
		this.pveUpgrade = pveUpgrade;
	}

	public int getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}

	
}
