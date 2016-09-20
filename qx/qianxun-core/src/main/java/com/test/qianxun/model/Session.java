package com.test.qianxun.model;

public class Session {
	private String id;
	private String uid;
	private String username;
	private String bind;//是否绑定邮箱

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		this.bind = bind;
	}
	
	
}