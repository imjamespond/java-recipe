package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("board")
public class Board {
	@Id("board_id_seq")
	@Column("id")
	private long id;
	/**
	 * 所属论坛id
	 */
	@Column("fid")
	private long fid;
	/**
	 * 所属论坛name
	 */
	@Column("fname")
	private String fname;
	@Column("name")
	private String name;
	@Column("description")
	private String description;
	@Column("sort")
	private int sort;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}