package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticNPC {

	// 编号
	private int id;
	// 名字
	private String name;

	public static StaticNPC get(int id) {
		return StaticDataManager.getInstance().get(StaticNPC.class, id);
	}
	
	public static Map<Integer,StaticNPC> getAll() {
		return StaticDataManager.getInstance().getMap(StaticNPC.class);
	}

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


}
