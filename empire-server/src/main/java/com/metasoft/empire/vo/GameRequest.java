package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;

@DescAnno("游戏请求")
public class GameRequest {
	private long uid;
	private int map;
	private int roles[];

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int[] getRoles() {
		return roles;
	}

	public void setRoles(int[] roles) {
		this.roles = roles;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}
	
}
