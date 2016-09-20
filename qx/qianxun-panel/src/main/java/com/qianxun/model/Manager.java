package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("manager")
public class Manager {
	@Id("manager_id_seq")
	@Column("id")
	private long id;
	@Column("username")
	private String username;
	@Column("password")
	private String password;
	@Column("salt")
	private String salt;
	@Column("email")
	private String email;
	@Column("state")
	private int state;
	/**
	 * 拥有的角色key，多个角色以逗号分割
	 */
	@Column("roles")
	private String roles;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
