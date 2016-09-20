package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("财富排行信息")
public class PlayerGoldVO {
	@DescAnno("Id")
	private long userId;
	@DescAnno("昵称")
	private String userName;
	@DescAnno("分数")
	private int gold;
	@DescAnno("等级")
	private int level;
	@DescAnno("0下线1在线 2在房间")
	private int online;
	@DescAnno("是否关注")
	private int follow;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getFollow() {
		return follow;
	}

	public void setFollow(int follow) {
		this.follow = follow;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
