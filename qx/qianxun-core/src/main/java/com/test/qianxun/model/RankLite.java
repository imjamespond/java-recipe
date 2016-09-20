package com.test.qianxun.model;

public class RankLite {
	private long gid;
	private String gname;
	private int type;
	private int rank;
	private int votes;
	private int rate;


	public RankLite(long gid, String gname, int type, int rank, int votes, int rate) {
		super();
		this.gid = gid;
		this.gname = gname;
		this.type = type;
		this.rank = rank;
		this.votes = votes;
		this.rate = rate;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}