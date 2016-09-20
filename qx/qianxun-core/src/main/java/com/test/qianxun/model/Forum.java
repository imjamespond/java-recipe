package com.test.qianxun.model;

import java.util.List;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("forum")
public class Forum {
	@Id("forum_id_seq")
	@Column("id")
	private long id;
	/**
	 * 对应产品id
	 */
	@Column("pid")
	private long pid;
	@Column("name")
	private String name;
	private List<Board> boarlList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Board> getBoarlList() {
		return boarlList;
	}

	public void setBoarlList(List<Board> boarlList) {
		this.boarlList = boarlList;
	}

}