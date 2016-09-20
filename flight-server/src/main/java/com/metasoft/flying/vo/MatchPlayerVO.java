package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("比赛排行信息")
public class MatchPlayerVO {
	@DescAnno("Id")
	private long userId;
	@DescAnno("昵称")
	private String userName;
	@DescAnno("分数")
	private int score;
	@DescAnno("比赛时间")
	private long elapsed;
	@DescAnno("上场比赛名次")
	private long rank;
	@DescAnno("等级分数")
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getElapsed() {
		return elapsed;
	}

	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

}
