package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("web_users")
public class User {
	@Id("web_users_id_seq")
	@Column("id")
	private long id;
	@Column("username")
	private String username;
	@Column("password")
	private String password;
	@Column("salt")
	private String salt;
	@Column("email")
	private String email;
	@Column("phone")
	private String phone;
	@Column("name")
	private String name;
	@Column("identity")
	private String identity;
	@Column("registertime")
	private long registerTime;
	@Column("activetime")
	private long activeTime;
	/**
	 * 防沉迷状态 0未通过 1通过
	 */
	@Column("state")
	private int state;
	/**
	 * 是否参加小公主活动 0未参加 1参加
	 */
	@Column("applestate")
	private int appleState;
	/**
	 * 账号类型 0正常注册 1系统小号
	 */
	@Column("accountstate")
	private int accountState;
	/**
	 * 小号参与投苹果时间
	 */
	@Column("appletime")
	private long appleTime;
	private int rose;
	private int apple;
	private String nickname;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}

	public int getApple() {
		return apple;
	}

	public void setApple(int apple) {
		this.apple = apple;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAppleState() {
		return appleState;
	}

	public void setAppleState(int appleState) {
		this.appleState = appleState;
	}

	public int getAccountState() {
		return accountState;
	}

	public void setAccountState(int accountState) {
		this.accountState = accountState;
	}

	public long getAppleTime() {
		return appleTime;
	}

	public void setAppleTime(long appleTime) {
		this.appleTime = appleTime;
	}

}