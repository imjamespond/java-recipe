package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("game_vote")
public class GameVote {
	
	//@Column("id")
	//private long id;
	@Id("game_vote_id_seq")
	@Column("gid")
	private long gid=0l;
	@Column("trend_1")
	private int trend_1=0;//-1下降 0平 1上升
	@Column("trend_2")
	private int trend_2=0;
	@Column("trend_3")
	private int trend_3=0;
	@Column("trend_4")
	private int trend_4=0;
	@Column("trend_5")
	private int trend_5=0;
	@Column("trend_6")
	private int trend_6=0;
	@Column("trend_7")
	private int trend_7=0;
	@Column("trend_8")
	private int trend_8=0;
	@Column("trend_9")
	private int trend_9=0;
	@Column("trend_0")
	private int trend_0=0;
	@Column("vote_1")
	private int vote_1=0;
	@Column("vote_2")
	private int vote_2=0;
	@Column("vote_3")
	private int vote_3=0;
	@Column("vote_4")
	private int vote_4=0;
	@Column("vote_5")
	private int vote_5=0;
	@Column("vote_6")
	private int vote_6=0;
	@Column("vote_7")
	private int vote_7=0;
	@Column("vote_8")
	private int vote_8=0;
	@Column("vote_9")
	private int vote_9=0;
	@Column("vote_0")
	private int vote_0=0;
	@Column("last_1")
	private int last_1=0;//上次排行,用于计算trend
	@Column("last_2")
	private int last_2=0;
	@Column("last_3")
	private int last_3=0;
	@Column("last_4")
	private int last_4=0;
	@Column("last_5")
	private int last_5=0;
	@Column("last_6")
	private int last_6=0;
	@Column("last_7")
	private int last_7=0;
	@Column("last_8")
	private int last_8=0;
	@Column("last_9")
	private int last_9=0;
	@Column("last_0")
	private int last_0=0;
	
	public GameVote(){
		
	}
	
	public GameVote(long id) {
		super();
		this.gid = id;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	
	public int getVote_1() {
		return vote_1;
	}

	public void addVote_1() {
		this.vote_1 = 1+vote_1;
	}

	public int getVote_2() {
		return vote_2;
	}

	public void addVote_2() {
		this.vote_2 = 1+vote_2;
	}

	public int getVote_3() {
		return vote_3;
	}

	public void addVote_3() {
		this.vote_3 = 1+vote_3;
	}

	public int getVote_4() {
		return vote_4;
	}

	public void addVote_4() {
		this.vote_4 = 1+vote_4;
	}

	public int getVote_5() {
		return vote_5;
	}

	public void addVote_5() {
		this.vote_5 = 1+vote_5;
	}

	public int getVote_6() {
		return vote_6;
	}

	public void addVote_6() {
		this.vote_6 = 1+vote_6;
	}

	public int getTrend_1() {
		return trend_1;
	}

	public void setTrend_1(int trend_1) {
		this.trend_1 = trend_1;
	}

	public int getTrend_2() {
		return trend_2;
	}

	public void setTrend_2(int trend_2) {
		this.trend_2 = trend_2;
	}

	public int getTrend_3() {
		return trend_3;
	}

	public void setTrend_3(int trend_3) {
		this.trend_3 = trend_3;
	}

	public int getTrend_4() {
		return trend_4;
	}

	public void setTrend_4(int trend_4) {
		this.trend_4 = trend_4;
	}

	public int getTrend_5() {
		return trend_5;
	}

	public void setTrend_5(int trend_5) {
		this.trend_5 = trend_5;
	}

	public int getTrend_6() {
		return trend_6;
	}

	public void setTrend_6(int trend_6) {
		this.trend_6 = trend_6;
	}

	public int getTrend_7() {
		return trend_7;
	}

	public void setTrend_7(int trend_7) {
		this.trend_7 = trend_7;
	}

	public int getTrend_8() {
		return trend_8;
	}

	public void setTrend_8(int trend_8) {
		this.trend_8 = trend_8;
	}

	public int getTrend_9() {
		return trend_9;
	}

	public void setTrend_9(int trend_9) {
		this.trend_9 = trend_9;
	}

	public int getTrend_0() {
		return trend_0;
	}

	public void setTrend_0(int trend_0) {
		this.trend_0 = trend_0;
	}

	public int getVote_7() {
		return vote_7;
	}

	public void addVote_7() {
		this.vote_7 = 1+vote_7;
	}

	public int getVote_8() {
		return vote_8;
	}

	public void addVote_8() {
		this.vote_8 = 1+vote_8;
	}

	public int getVote_9() {
		return vote_9;
	}

	public void addVote_9() {
		this.vote_9 = 1+vote_9;
	}

	public int getVote_0() {
		return vote_0;
	}

	public void addVote_0() {
		this.vote_0 = 1+vote_0;
	}

	public int getLast_1() {
		return last_1;
	}

	public void setLast_1(int last_1) {
		this.last_1 = last_1;
	}

	public int getLast_2() {
		return last_2;
	}

	public void setLast_2(int last_2) {
		this.last_2 = last_2;
	}

	public int getLast_3() {
		return last_3;
	}

	public void setLast_3(int last_3) {
		this.last_3 = last_3;
	}

	public int getLast_4() {
		return last_4;
	}

	public void setLast_4(int last_4) {
		this.last_4 = last_4;
	}

	public int getLast_5() {
		return last_5;
	}

	public void setLast_5(int last_5) {
		this.last_5 = last_5;
	}

	public int getLast_6() {
		return last_6;
	}

	public void setLast_6(int last_6) {
		this.last_6 = last_6;
	}

	public int getLast_7() {
		return last_7;
	}

	public void setLast_7(int last_7) {
		this.last_7 = last_7;
	}

	public int getLast_8() {
		return last_8;
	}

	public void setLast_8(int last_8) {
		this.last_8 = last_8;
	}

	public int getLast_9() {
		return last_9;
	}

	public void setLast_9(int last_9) {
		this.last_9 = last_9;
	}

	public int getLast_0() {
		return last_0;
	}

	public void setLast_0(int last_0) {
		this.last_0 = last_0;
	}



	public void setVote_1(int vote_1) {
		this.vote_1 = vote_1;
	}

	public void setVote_2(int vote_2) {
		this.vote_2 = vote_2;
	}

	public void setVote_3(int vote_3) {
		this.vote_3 = vote_3;
	}

	public void setVote_4(int vote_4) {
		this.vote_4 = vote_4;
	}

	public void setVote_5(int vote_5) {
		this.vote_5 = vote_5;
	}

	public void setVote_6(int vote_6) {
		this.vote_6 = vote_6;
	}

	public void setVote_7(int vote_7) {
		this.vote_7 = vote_7;
	}

	public void setVote_8(int vote_8) {
		this.vote_8 = vote_8;
	}

	public void setVote_9(int vote_9) {
		this.vote_9 = vote_9;
	}

	public void setVote_0(int vote_0) {
		this.vote_0 = vote_0;
	}

	
}