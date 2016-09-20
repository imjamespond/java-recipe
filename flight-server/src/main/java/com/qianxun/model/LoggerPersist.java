package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("web_logger")
public class LoggerPersist {

	@Id("logger_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("logdate")
	private long logDate;
	@Column("num")
	private int num;
	@Column("current")
	private int current;
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	@Column("type")
	private int type;//
	@Column("discription")
	private String discription;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLogDate() {
		return logDate;
	}

	public void setLogDate(long logDate) {
		this.logDate = logDate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	

}
