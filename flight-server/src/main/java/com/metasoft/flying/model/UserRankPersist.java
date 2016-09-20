package com.metasoft.flying.model;

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
		match = new Integer(0);
		elapsed = new Long(0);
	}
	//use object type for join query
	@Id("fly_rank_seq")
	@Column("id")
	private Long id;
	@Column("uid")
	private Long uid;
	@Column("datenum")
	private Integer datenum;
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
	@Column("match")
	private Integer match;
	@Column("elapsed")
	private long elapsed;

	// @Column("username")//left join
	private String username;
	private Integer sumnum;
	private Long fuid;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public Integer getSumnum() {
		return sumnum;
	}

	public void setSumnum(Integer sumnum) {
		this.sumnum = sumnum;
	}

	public Long getFuid() {
		return fuid;
	}

	public void setFuid(Long fuid) {
		this.fuid = fuid;
	}

	public Integer getDatenum() {
		return datenum;
	}

	public void setDatenum(Integer datenum) {
		this.datenum = datenum;
	}

	public Integer getMatch() {
		return match;
	}

	public void setMatch(Integer match) {
		this.match = match;
	}

	public long getElapsed() {
		return elapsed;
	}

	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}

}
