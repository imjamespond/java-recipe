package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticMission {
	// 任务编号
	private int id;
	// 任务名字
	private String name;
	// 对战次数
	private int condition;
	// 下个任务id
	private int next;
	// 类型
	private int type;
	// 奖励金币
	private int money;
	
	public static StaticMission get(int id) {
		return StaticDataManager.getInstance().get(StaticMission.class, id);
	}
	
	public static Map<Integer,StaticMission> getAll() {
		return StaticDataManager.getInstance().getMap(StaticMission.class);
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

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
