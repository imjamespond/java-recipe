package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticStrings {

	// 编号
	private int id;
	// 版本
	private String name;


	public static StaticStrings get(int id) {
		return StaticDataManager.getInstance().get(StaticStrings.class, id);
	}
	
	public static Map<Integer,StaticStrings> getAll() {
		return StaticDataManager.getInstance().getMap(StaticStrings.class);
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
