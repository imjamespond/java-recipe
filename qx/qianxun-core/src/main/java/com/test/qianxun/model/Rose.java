package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("rose")
public class Rose {
	@Id("rose_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("timeline")
	private long timeline;
	/**
	 * 兑换礼品的ID
	 */
	@Column("gid")
	private long gid;
	/**
	 * 玫瑰变化数量 正表示增加 负表示减少
	 */
	@Column("count")
	private int count;
	/**
	 * 玫瑰变化描述
	 */
	@Column("discription")
	private String discription;
	/**
	 * 送货地址
	 */
	@Column("address")
	private String address;
	/**
	 * 收件人手机号
	 */
	@Column("phone")
	private String phone;
	/**
	 * 收件人姓名
	 */
	@Column("addressee")
	private String addressee;

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

	public long getTimeline() {
		return timeline;
	}

	public void setTimeline(long timeline) {
		this.timeline = timeline;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
}