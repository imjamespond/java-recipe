package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("game")
public class Game {
	@Id("game_id_seq")
	@Column("id")
	private long id;
	@Column("gname")
	protected String gname;
	@Column("type")
	protected int type;
	@Column("state")
	protected int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}


}