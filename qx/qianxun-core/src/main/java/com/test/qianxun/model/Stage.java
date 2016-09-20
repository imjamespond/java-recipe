package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("stage")
public class Stage {
	@Id("stage_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("mood")
	private String mood;
	@Column("username")
	private String username;
	@Column("nickname")
	private String nickname;
	/**
	 * 状体 0正常1禁用
	 */
	@Column("state")
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}