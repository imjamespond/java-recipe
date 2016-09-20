package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

/**
 * @author james
 *兑换记录
 */
@Table("web_exchange")
public class Exchange {
	@Id("web_exchange_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	protected long uid;
	@Column("time_")
	protected long time_;
	@Column("type_")
	protected int type_;
	@Column("item")
	protected int item;//兑换物品
	@Column("num")
	protected int num;
	@Column("invoice")
	protected String invoice;
	@Column("remark")
	protected String remark;

	public Exchange() {
		super();
	}

	public Exchange(long uid) {
		super();
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time_;
	}

	public void setTime(long time_) {
		this.time_ = time_;
	}

	public int getType() {
		return type_;
	}

	public void setType(int type_) {
		this.type_ = type_;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getTime_() {
		return time_;
	}

	public void setTime_(long time_) {
		this.time_ = time_;
	}

	public int getType_() {
		return type_;
	}

	public void setType_(int type_) {
		this.type_ = type_;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}


}