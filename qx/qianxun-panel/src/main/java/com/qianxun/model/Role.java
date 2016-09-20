package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("role")
public class Role {
	@Id("manager_id_seq")
	@Column("id")
	private long id;
	/**
	 * 角色名称
	 */
	@Column("name")
	private String name;
	/**
	 * 角色在程序代码中key值
	 */
	@Column("key")
	private String key;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
