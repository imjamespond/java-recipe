package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticDistrictTypes {
	// 区编号
	private int id;
	// 区名字
	private String name;
	// 比牌时间
	private int comparisonTime;
	// 最少金币限制
	private int moneyLimitation;
	// 最大金币限制 
	private int moneyMaxLimitation;
	// 兑换率
	private int rateOfExchange;
	// 每局抽取金币
	private int tax;
	
	public static StaticDistrictTypes get(int id) {
		return StaticDataManager.getInstance().get(StaticDistrictTypes.class, id);
	}

	public static Map<Integer,StaticDistrictTypes> getAll() {
		return StaticDataManager.getInstance().getMap(StaticDistrictTypes.class);
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

	public int getComparisonTime() {
		return comparisonTime;
	}

	public void setComparisonTime(int comparisonTime) {
		this.comparisonTime = comparisonTime;
	}

	public int getMoneyLimitation() {
		return moneyLimitation;
	}

	public void setMoneyLimitation(int moneyLimitation) {
		this.moneyLimitation = moneyLimitation;
	}

	public int getMoneyMaxLimitation() {
		return moneyMaxLimitation;
	}

	public void setMoneyMaxLimitation(int moneyMaxLimitation) {
		this.moneyMaxLimitation = moneyMaxLimitation;
	}
	
	public int getRateOfExchange() {
		return rateOfExchange;
	}

	public void setRateOfExchange(int rateOfExchange) {
		this.rateOfExchange = rateOfExchange;
	}
	
	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

}