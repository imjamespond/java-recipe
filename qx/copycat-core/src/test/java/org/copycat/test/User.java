package org.copycat.test;

import org.copycat.framework.annotation.Collection;
import org.copycat.framework.annotation.Element;

@Collection("user")
public class User {
	@Element("_id")
	private String id;
	@Element("uname")
	private String username;
	@Element("pword")
	private String password;
	@Element("time")
	private long time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
