package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticSpecialCardScore {
	// 特殊牌编号
	private int id;
	// 
	private String name;
	// 特殊牌积分
	private int score;


	
	public static StaticSpecialCardScore get(int id) {
		return StaticDataManager.getInstance().get(StaticSpecialCardScore.class, id);
	}
	
	public static Map<Integer,StaticSpecialCardScore> getAll() {
		return StaticDataManager.getInstance().getMap(StaticSpecialCardScore.class);
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


}