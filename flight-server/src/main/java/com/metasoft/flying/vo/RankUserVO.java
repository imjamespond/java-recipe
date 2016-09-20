package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("排行信息")
public class RankUserVO implements Comparable<RankUserVO> {
	@DescAnno("Id")
	private long userId;
	@DescAnno("昵称")
	private String userName;
	@DescAnno("玫瑰或苹果")
	private int rose;
	@DescAnno("贡献")
	private int contribute;
	@DescAnno("0下线1在线 2在房间")
	private int online;
	@DescAnno("是否关注")
	private int follow;

	@Override
	public int compareTo(RankUserVO arg0) {
		if (rose < arg0.getRose()) {
			return 1;
		}
		return -1;
	}
	
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

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}

	public int getContribute() {
		return contribute;
	}

	public void setContribute(int contribute) {
		this.contribute = contribute;
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
