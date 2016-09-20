package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("topic")
public class Topic {
	@Id("topic_id_seq")
	@Column("id")
	private long id;
	/**
	 * 所属版块 1美女专区 2系统公告 3玩家交流
	 */
	@Column("stype")
	private int stype;
	/**
	 * 是否置顶 0否1是
	 */
	@Column("top")
	private int top;
	@Column("uid")
	private long uid;
	@Column("username")
	private String username;
	@Column("title")
	private String title;
	@Column("content")
	private String content;
	@Column("createtime")
	private long createTime;
	@Column("replytime")
	private long replyTime;
	/**
	 * 最新回复 username
	 */
	@Column("lastreply")
	private String lastReply;
	@Column("read")
	private int read;
	@Column("reply")
	private int reply;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStype() {
		return stype;
	}

	public void setStype(int stype) {
		this.stype = stype;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(long replyTime) {
		this.replyTime = replyTime;
	}

	public String getLastReply() {
		return lastReply;
	}

	public void setLastReply(String lastReply) {
		this.lastReply = lastReply;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getReply() {
		return reply;
	}

	public void setReply(int reply) {
		this.reply = reply;
	}
}