package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("charge")
public class Charge {
	@Id("charge_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("timeline")
	private long timeline;
	/**
	 * 支付宝流水号
	 */
	@Column("alipay_id")
	private String alipayId;
	/**
	 * 充值金额 精度为分
	 */
	@Column("count")
	private long count;
	/**
	 * 充值状态 0充值中 1充值成功 2充值失败
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
	
	public long getTimeline() {
		return timeline;
	}

	public void setTimeline(long timeline) {
		this.timeline = timeline;
	}

	public String getAlipayId() {
		return alipayId;
	}

	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}