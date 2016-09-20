package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("game")
public class GameEx extends Game {	
	@Id("game_id_seq")
	@Column("id")
	private long id;
	//for rank
	private Integer vote;
	private Integer rank;
	//for panel
	private Long gid;
	private Integer trend_1;
	private Integer trend_2;
	private Integer trend_3;
	private Integer trend_4;
	private Integer trend_5;
	private Integer trend_6;
	private Integer trend_7;
	private Integer trend_8;
	private Integer trend_9;
	private Integer trend_0;
	private Integer vote_1;
	private Integer vote_2;
	private Integer vote_3;
	private Integer vote_4;
	private Integer vote_5;
	private Integer vote_6;
	private Integer vote_7;
	private Integer vote_8;
	private Integer vote_9;
	private Integer vote_0;
	private Integer last_1;
	private Integer last_2;
	private Integer last_3;
	private Integer last_4;
	private Integer last_5;
	private Integer last_6;
	private Integer last_7;
	private Integer last_8;
	private Integer last_9;
	private Integer last_0;

	

	public Integer getTrend_1() {
		return trend_1;
	}

	public void setTrend_1(Integer trend_1) {
		this.trend_1 = trend_1;
	}

	public Integer getTrend_2() {
		return trend_2;
	}

	public void setTrend_2(Integer trend_2) {
		this.trend_2 = trend_2;
	}

	public Integer getTrend_3() {
		return trend_3;
	}

	public void setTrend_3(Integer trend_3) {
		this.trend_3 = trend_3;
	}

	public Integer getTrend_4() {
		return trend_4;
	}

	public void setTrend_4(Integer trend_4) {
		this.trend_4 = trend_4;
	}

	public Integer getTrend_5() {
		return trend_5;
	}

	public void setTrend_5(Integer trend_5) {
		this.trend_5 = trend_5;
	}

	public Integer getTrend_6() {
		return trend_6;
	}

	public void setTrend_6(Integer trend_6) {
		this.trend_6 = trend_6;
	}

	public Integer getTrend_7() {
		return trend_7;
	}

	public void setTrend_7(Integer trend_7) {
		this.trend_7 = trend_7;
	}

	public Integer getTrend_8() {
		return trend_8;
	}

	public void setTrend_8(Integer trend_8) {
		this.trend_8 = trend_8;
	}

	public Integer getTrend_9() {
		return trend_9;
	}

	public void setTrend_9(Integer trend_9) {
		this.trend_9 = trend_9;
	}

	public Integer getTrend_0() {
		return trend_0;
	}

	public void setTrend_0(Integer trend_0) {
		this.trend_0 = trend_0;
	}

	public Integer getVote_1() {
		return vote_1;
	}

	public void setVote_1(Integer vote_1) {
		this.vote_1 = vote_1;
	}

	public Integer getVote_2() {
		return vote_2;
	}

	public void setVote_2(Integer vote_2) {
		this.vote_2 = vote_2;
	}

	public Integer getVote_3() {
		return vote_3;
	}

	public void setVote_3(Integer vote_3) {
		this.vote_3 = vote_3;
	}

	public Integer getVote_4() {
		return vote_4;
	}

	public void setVote_4(Integer vote_4) {
		this.vote_4 = vote_4;
	}

	public Integer getVote_5() {
		return vote_5;
	}

	public void setVote_5(Integer vote_5) {
		this.vote_5 = vote_5;
	}

	public Integer getVote_6() {
		return vote_6;
	}

	public void setVote_6(Integer vote_6) {
		this.vote_6 = vote_6;
	}

	public Integer getVote_7() {
		return vote_7;
	}

	public void setVote_7(Integer vote_7) {
		this.vote_7 = vote_7;
	}

	public Integer getVote_8() {
		return vote_8;
	}

	public void setVote_8(Integer vote_8) {
		this.vote_8 = vote_8;
	}

	public Integer getVote_9() {
		return vote_9;
	}

	public void setVote_9(Integer vote_9) {
		this.vote_9 = vote_9;
	}

	public Integer getVote_0() {
		return vote_0;
	}

	public void setVote_0(Integer vote_0) {
		this.vote_0 = vote_0;
	}

	public Integer getLast_1() {
		return last_1;
	}

	public void setLast_1(Integer last_1) {
		this.last_1 = last_1;
	}

	public Integer getLast_2() {
		return last_2;
	}

	public void setLast_2(Integer last_2) {
		this.last_2 = last_2;
	}

	public Integer getLast_3() {
		return last_3;
	}

	public void setLast_3(Integer last_3) {
		this.last_3 = last_3;
	}

	public Integer getLast_4() {
		return last_4;
	}

	public void setLast_4(Integer last_4) {
		this.last_4 = last_4;
	}

	public Integer getLast_5() {
		return last_5;
	}

	public void setLast_5(Integer last_5) {
		this.last_5 = last_5;
	}

	public Integer getLast_6() {
		return last_6;
	}

	public void setLast_6(Integer last_6) {
		this.last_6 = last_6;
	}

	public Integer getLast_7() {
		return last_7;
	}

	public void setLast_7(Integer last_7) {
		this.last_7 = last_7;
	}

	public Integer getLast_8() {
		return last_8;
	}

	public void setLast_8(Integer last_8) {
		this.last_8 = last_8;
	}

	public Integer getLast_9() {
		return last_9;
	}

	public void setLast_9(Integer last_9) {
		this.last_9 = last_9;
	}

	public Integer getLast_0() {
		return last_0;
	}

	public void setLast_0(Integer last_0) {
		this.last_0 = last_0;
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}


}