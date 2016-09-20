package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("申请下棋")
public class ChessApplicationVO {

	public ChessApplicationVO() {

	}

	@DescAnno("id")
	public long userId;
	@DescAnno("玩家")
	public String userName;
	@DescAnno("申请下棋时间")
	public long appTime;
	@DescAnno("玫瑰")
	public int rose;
	@DescAnno("0下线1在线 2在房间")
	public int online;
	@DescAnno("1.自己取消，2.房主取消，3.飞行器  4对战 5 第三款游戏")
	public int type;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAppTime() {
		return appTime;
	}

	public void setAppTime(long appTime) {
		this.appTime = appTime;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



}