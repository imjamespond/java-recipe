package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("比赛排行信息")
public class PlayerPveVO {
	@DescAnno("Id")
	private long userId;
	@DescAnno("昵称")
	private String userName;
	@DescAnno("闯关时间")
	private long pvetime;
	@DescAnno("闯关数")
	private long pve;
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

	public long getPvetime() {
		return pvetime;
	}

	public void setPvetime(long pvetime) {
		this.pvetime = pvetime;
	}

	public long getPve() {
		return pve;
	}

	public void setPve(long pve) {
		this.pve = pve;
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

}
