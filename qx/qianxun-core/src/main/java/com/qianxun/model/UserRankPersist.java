package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("fly_rank")
public class UserRankPersist {
	public UserRankPersist() {

	}

	public UserRankPersist(long uid) {
		super();
		this.uid = uid;
		weeknum = new Integer(0);
		monthnum = new Integer(0);
		yearnum = new Integer(0);
		contribute = new Integer(0);
		charm = new Integer(0);
		apple_in = new Integer(0);
		apple_out = new Integer(0);
		prizeid = new Integer(0);
	}
	//use object type for join query
	@Id("fly_rank_seq")
	@Column("id")
	private Long id;
	@Column("uid")
	private Long uid;
	@Column("prizeid")
	private Integer prizeid;
	@Column("weeknum")
	private Integer weeknum;
	@Column("monthnum")
	private Integer monthnum;
	@Column("yearnum")
	private Integer yearnum;
	@Column("contribute")
	private Integer contribute;
	@Column("charm")
	private Integer charm;
	@Column("apple_in")
	private Integer apple_in;
	@Column("apple_out")
	private Integer apple_out;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getWeeknum() {
		return weeknum;
	}

	public void setWeeknum(Integer weeknum) {
		this.weeknum = weeknum;
	}

	public Integer getMonthnum() {
		return monthnum;
	}

	public void setMonthnum(Integer monthnum) {
		this.monthnum = monthnum;
	}

	public Integer getYearnum() {
		return yearnum;
	}

	public void setYearnum(Integer yearnum) {
		this.yearnum = yearnum;
	}

	public Integer getContribute() {
		return contribute;
	}

	public void setContribute(Integer contribute) {
		this.contribute = contribute;
	}

	public Integer getCharm() {
		return charm;
	}

	public void setCharm(Integer charm) {
		this.charm = charm;
	}

	public Integer getApple_in() {
		return apple_in;
	}

	public void setApple_in(Integer apple_in) {
		this.apple_in = apple_in;
	}

	public Integer getApple_out() {
		return apple_out;
	}

	public void setApple_out(Integer apple_out) {
		this.apple_out = apple_out;
	}

	public Integer getPrizeid() {
		return prizeid;
	}

	public void setPrizeid(Integer prizeid) {
		this.prizeid = prizeid;
	}


}
