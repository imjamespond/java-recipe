package com.qianxun.model.job;


public class VoteJob {

	private long uid;
	private long gid;
	private int type;	
	private int rank;	


	public VoteJob(long uid, long gid, int type, int rank) {
		super();
		this.uid = uid;
		this.gid = gid;
		this.rank = rank;
		this.type = type;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}




}
