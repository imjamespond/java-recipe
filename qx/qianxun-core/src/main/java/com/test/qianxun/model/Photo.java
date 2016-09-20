package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("photo")
public class Photo {
	@Id("photo_id_seq")
	@Column("id")
	private long id;
	/**
	 * 所属stageid
	 */
	@Column("sid")
	private long sid;
	@Column("width")
	private long width;
	@Column("height")
	private long height;
	@Column("name")
	private long name;
	@Column("type")
	private String type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public long getName() {
		return name;
	}

	public void setName(long name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}