package com.metasoft.flying.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.flying.net.annotation.DescAnno;

@Table("/data/PVE_NPC.xls")
public class PveNpcData{
	@DescAnno("id")
	protected int id;
	@DescAnno("用户名")
	protected String name;
	@DescAnno("昵称")
	protected String nickname;
	@DescAnno("经验")
	protected int level;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	
}
