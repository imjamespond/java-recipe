package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

/**
 * 用户登陆ip记录
 * 
 * @author zhangcj
 *
 */
@Table("useriprecord")
public class UserIpRecord {
	@Id("useriprecord_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	/**
	 * 登陆ip
	 */
	@Column("ip")
	private String ip;
	/**
	 * 该ip登陆次数
	 */
	@Column("frequency")
	private int frequency;

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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}