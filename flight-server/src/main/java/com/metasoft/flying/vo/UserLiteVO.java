package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("简单用户信息")
public class UserLiteVO {

	@DescAnno("id")
	private long userId;
	@DescAnno("昵称")
	private String name;
	@DescAnno("关注度")
	private int count;
	@DescAnno("0下线1在线 2在房间")
	private int online;

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

}
