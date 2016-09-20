package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("vote")
public class Vote {
	@Id("vote_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("gid")
	private long gid;
	@Column("gname")
	private String gname;
	@Column("type")
	private int type;
	@Column("rank")
	private int rank;
	@Column("votetime")
	private long votetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
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

	public long getVotetime() {
		return votetime;
	}

	public void setVotetime(long votetime) {
		this.votetime = votetime;
	}
}