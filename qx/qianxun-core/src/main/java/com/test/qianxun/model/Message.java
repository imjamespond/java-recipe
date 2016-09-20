package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("message")
public class Message {
	@Id("message_id_seq")
	@Column("id")
	private long id;
	@Column("uid")
	private long uid;
	@Column("content")
	private String content;
	@Column("discription")
	private String discription;
	@Column("url")
	private String url;
	/**
	 * 消息状态 0未查看 1已查看
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}