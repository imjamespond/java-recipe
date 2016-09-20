package com.test.qianxun.model;

import java.util.List;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("reply")
public class Reply {
	@Id("reply_id_seq")
	@Column("id")
	private long id;
	@Column("tid")
	private long tid;
	@Column("uid")
	private long uid;
	@Column("username")
	private String username;
	@Column("content")
	private String content;
	@Column("createTime")
	private long createTime;
	@Column("floor")
	private int floor;
	private List<Comment> commentList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
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

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
}