package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("rank")
public class Rank {
	@Id("rank_id_seq")
	@Column("id")
	private long id;
	@Column("gid")
	private long gid;
	@Column("gname")
	private String gname;
	@Column("type")
	private int type;
	@Column("rank")
	private int rank;
	@Column("votes")
	private int votes;
	/**
	 * 初始排名
	 */
	@Column("base")
	private int base;
	/**
	 * 排名变化
	 */
	@Column("rate")
	private int rate;
	/**
	 * 状态 0表示作废 1表示使用
	 */
	@Column("state")
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}