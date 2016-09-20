package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticMatch {

	// 编号
	private int id;
	// 版本
	private String name;
	
	private String date;
	
	private int hour;
	
	private int minute;
	
	private int cost;
	
	private int icon;
	
	private int initCounter;
	
	private int minPlayer;
	
	private int maxPlayer;
	
	private int initRate;
	
	private int deltaRate;

	private int t1;
	private int n1;
	private int t2;
	private int n2;
	private int t3;
	private int n3;
	private int t4;
	private int n4;
	private int t5;
	private int n5;
	private int t6;
	private int n6;
	private int t7;
	private int n7;
	private int t8;
	private int n8;
	private int t9;
	private int n9;
	private int t10;
	private int n10;
	
	public static StaticMatch get(int id) {
		return StaticDataManager.getInstance().get(StaticMatch.class, id);
	}
	
	public static Map<Integer,StaticMatch> getAll() {
		return StaticDataManager.getInstance().getMap(StaticMatch.class);
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getInitCounter() {
		return initCounter;
	}

	public void setInitCounter(int initCounter) {
		this.initCounter = initCounter;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public void setMinPlayer(int minPlayer) {
		this.minPlayer = minPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	public int getInitRate() {
		return initRate;
	}

	public void setInitRate(int initRate) {
		this.initRate = initRate;
	}

	public int getDeltaRate() {
		return deltaRate;
	}

	public void setDeltaRate(int deltaRate) {
		this.deltaRate = deltaRate;
	}

	public int getT1() {
		return t1;
	}

	public void setT1(int t1) {
		this.t1 = t1;
	}

	public int getN1() {
		return n1;
	}

	public void setN1(int n1) {
		this.n1 = n1;
	}

	public int getT2() {
		return t2;
	}

	public void setT2(int t2) {
		this.t2 = t2;
	}

	public int getN2() {
		return n2;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public int getT3() {
		return t3;
	}

	public void setT3(int t3) {
		this.t3 = t3;
	}

	public int getN3() {
		return n3;
	}

	public void setN3(int n3) {
		this.n3 = n3;
	}

	public int getT4() {
		return t4;
	}

	public void setT4(int t4) {
		this.t4 = t4;
	}

	public int getN4() {
		return n4;
	}

	public void setN4(int n4) {
		this.n4 = n4;
	}

	public int getT5() {
		return t5;
	}

	public void setT5(int t5) {
		this.t5 = t5;
	}

	public int getN5() {
		return n5;
	}

	public void setN5(int n5) {
		this.n5 = n5;
	}

	public int getT6() {
		return t6;
	}

	public void setT6(int t6) {
		this.t6 = t6;
	}

	public int getN6() {
		return n6;
	}

	public void setN6(int n6) {
		this.n6 = n6;
	}

	public int getT7() {
		return t7;
	}

	public void setT7(int t7) {
		this.t7 = t7;
	}

	public int getN7() {
		return n7;
	}

	public void setN7(int n7) {
		this.n7 = n7;
	}

	public int getT8() {
		return t8;
	}

	public void setT8(int t8) {
		this.t8 = t8;
	}

	public int getN8() {
		return n8;
	}

	public void setN8(int n8) {
		this.n8 = n8;
	}

	public int getT9() {
		return t9;
	}

	public void setT9(int t9) {
		this.t9 = t9;
	}

	public int getN9() {
		return n9;
	}

	public void setN9(int n9) {
		this.n9 = n9;
	}

	public int getT10() {
		return t10;
	}

	public void setT10(int t10) {
		this.t10 = t10;
	}

	public int getN10() {
		return n10;
	}

	public void setN10(int n10) {
		this.n10 = n10;
	}

}
