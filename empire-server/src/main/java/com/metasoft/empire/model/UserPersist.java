package com.metasoft.empire.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("users")
public class UserPersist {
	public UserPersist() {
	}

	public UserPersist(long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}

	@Id("user_id_seq")
	@Column("id")
	private long id;
	@Column("logindate")
	private long loginDate;
	@Column("username")
	private String username = "";
	@Column("passwd")
	private String passwd = "";
	@Column("nickname")
	private String nickname = "";
	@Column("email")
	private String email = "";
	@Column("imei")
	private String imei = "";//International Mobile Equipment Identity
	@Column("avatar")
	private String avatar = "";

	@Column("gender")
	private int gender;// 性别1男2女

	@Column("consecutive")
	private int consecutive;//连续登录天数
	@Column("totaldays")
	private int totaldays;//总登录天数
	@Column("totaltime")
	private long totaltime;//总在线时长
	@Column("createdate")
	private long createdate;//建号时间

	

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

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(long loginDate) {
		this.loginDate = loginDate;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getConsecutive() {
		return consecutive;
	}

	public void setConsecutive(int consecutive) {
		this.consecutive = consecutive;
	}

	public int getTotaldays() {
		return totaldays;
	}

	public void setTotaldays(int totaldays) {
		this.totaldays = totaldays;
	}

	public long getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(long totaltime) {
		this.totaltime = totaltime;
	}

	public long getCreatedate() {
		return createdate;
	}

	public void setCreatedate(long createdate) {
		this.createdate = createdate;
	}

}
/* auto generated code */
/* end generated */
