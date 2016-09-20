package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

/**
 * 用户登录记录
 * 
 * @author james
 *
 */
@Table("web_signin")
public class SigninRecord {
	@Id("web_signin_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	/**
	 * 登录时间
	 */
	@Column("date_")
	private long date_;
	/**
	 * 登录ip
	 */
	@Column("ip")
	private String ip;


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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getDate_() {
		return date_;
	}

	public void setDate_(long date_) {
		this.date_ = date_;
	}


}