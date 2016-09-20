package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("web_payment")
public class PaymentPersist {
	@Id("pay_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("payment")
	private double payment;//支付额
	@Column("type")
	private int type;//1为支付宝2为易宝
	@Column("state")
	private int state;//订单说明0进行中1完成2失败
	@Column("order_id")
	private String orderId;//订单唯一单号
	@Column("description")
	private String description;//订单描述,一般为支付宝的流水号
	@Column("create_time")
	private long createTime;//订单创建时间
	@Column("finish_time")
	private long finish_time;//订单结束时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(long finish_time) {
		this.finish_time = finish_time;
	}
	public double getPayment() {
		return payment;
	}
	public void setPayment(double payment) {
		this.payment = payment;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	
}