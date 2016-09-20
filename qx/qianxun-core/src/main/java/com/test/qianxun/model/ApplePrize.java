package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("appleprize")
public class ApplePrize {
	@Id("appleprize_id_seq")
	@Column("id")
	private long id;
	/**
	 * 活动期数 从1递增
	 */
	@Column("prizeid")
	private int prizeid;
	/**
	 * 获奖人领奖姓名
	 */
	@Column("name")
	private String name;
	/**
	 * 获奖人领奖电话
	 */
	@Column("phone")
	private String phone;
	/**
	 * 获奖人领奖地址
	 */
	@Column("address")
	private String address;
	/**
	 * 本期活动描述 如第一期（3月22日---4月4日）
	 */
	@Column("description")
	private String description;
	/**
	 * 本期活动开始时间
	 */
	@Column("starttime")
	private long start;
	/**
	 * 本期活动结束时间
	 */
	@Column("endtime")
	private long end;
	/**
	 * 获奖人id
	 */
	@Column("uid")
	private long uid;
	/**
	 * 获奖人游戏昵称
	 */
	@Column("nickname")
	private String nickname;
	/**
	 * 获奖人获得苹果数量
	 */
	@Column("applenumber")
	private int appleNumber;
	/**
	 * 本期活动状态 0进行中 1等待领奖 2结束 3已创建未部署
	 */
	@Column("state")
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPrizeid() {
		return prizeid;
	}

	public void setPrizeid(int prizeid) {
		this.prizeid = prizeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAppleNumber() {
		return appleNumber;
	}

	public void setAppleNumber(int appleNumber) {
		this.appleNumber = appleNumber;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
