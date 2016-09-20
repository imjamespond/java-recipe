package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("web_match")
public class LoggerMatchPersist{


	@Id("web_match_id_seq")
	@Column("id")
	private Long id;
	@Column("uid")
	private Long uid;
	@Column("mid")
	private Integer mid;
	@Column("rank")
	private Integer rank;
	@Column("time")
	private Long time;
	public LoggerMatchPersist() {

	}
	public LoggerMatchPersist(Long uid, Integer mid, Integer rank, Long time) {
		super();
		this.uid = uid;
		this.mid = mid;
		this.rank = rank;
		this.time = time;
	}
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

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
