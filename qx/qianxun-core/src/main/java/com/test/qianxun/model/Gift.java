package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("gift")
public class Gift {
	@Id("gift_id_seq")
	@Column("id")
	private long id;
	/**
	 * 礼品名称
	 */
	@Column("gname")
	private String gname;
	/**
	 * 礼品所需玫瑰数
	 */
	@Column("count")
	private int count;
	/**
	 * 计量单位 如部 台 个
	 */
	@Column("unit")
	private String unit;
	/**
	 * 状态 1可用 0不可用
	 */
	@Column("state")
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}