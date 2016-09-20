package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("web_vote")
public class VoteLite {
	@Id("web_vote_id_seq")
	@Column("uid")
	private long uid;
	@Column("vote")
	private String vote="[]";//json表示游戏id数组//用位表示1<<type(0~3)*rank(0~9)
	@Column("epochweek")
	private int epochweek;

	public VoteLite() {
		super();
	}

	public VoteLite(long uid) {
		super();
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public int getEpochweek() {
		return epochweek;
	}

	public void setEpochweek(int epochweek) {
		this.epochweek = epochweek;
	}


}