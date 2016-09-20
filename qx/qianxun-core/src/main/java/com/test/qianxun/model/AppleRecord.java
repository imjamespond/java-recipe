package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("applerecord")
public class AppleRecord {
	@Id("applerecord_id_seq")
	@Column("id")
	private long id;
	/**
	 * 赠送人id
	 */
	@Column("fromuid")
	private long fromuid;
	/**
	 * 赠送人游戏昵称
	 */
	@Column("nickname")
	private String nickname;
	/**
	 * 被赠送人id
	 */
	@Column("touid")
	private long touid;
	@Column("number")
	private int number;
	@Column("time")
	private long time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFromuid() {
		return fromuid;
	}

	public void setFromuid(long fromuid) {
		this.fromuid = fromuid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getTouid() {
		return touid;
	}

	public void setTouid(long touid) {
		this.touid = touid;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}