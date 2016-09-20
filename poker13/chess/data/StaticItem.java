package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 物品静态数据
 * 
 * @author open
 * 
 */
public class StaticItem implements BroadcastMessage{
	// 物品编号
	private int id;
	// 名字
	private String name;
	private int price;
	private int type;
	private int skinid;
	// 描述
	private String description;
	// 系统回收价格
	// buff
	private long buffVip = 0;
	private long buffSafebox = 0;
	private long buffExpression = 0;
	private long buffVipExpression = 0;
	private long buffLoginMoneyGiving = 0;
	private int loginMoneyGiving = 0;
	private int moneyGiving = 0;
	private long buffMultiplePoint = 0;
	// times
	private int speaker = 0;

	private int clearPoint=0;
	private int clearTimes=0;
	private int cost=0;
	private int discount=0;
	private int allowPurchace=0;

	
	public static StaticItem get(int id) {
		return StaticDataManager.getInstance().get(StaticItem.class, id);
	}

	public static Map<Integer, StaticItem> getAll() {
		return StaticDataManager.getInstance().getMap(StaticItem.class);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getBuffVip() {
		return buffVip;
	}

	public void setBuffVip(long buffVip) {
		this.buffVip = buffVip;
	}

	public long getBuffSafebox() {
		return buffSafebox;
	}

	public void setBuffSafebox(long buffSafebox) {
		this.buffSafebox = buffSafebox;
	}

	public long getBuffExpression() {
		return buffExpression;
	}

	public void setBuffExpression(long buffExpression) {
		this.buffExpression = buffExpression;
	}

	public long getBuffVipExpression() {
		return buffVipExpression;
	}

	public void setBuffVipExpression(long buffVipExpression) {
		this.buffVipExpression = buffVipExpression;
	}

	public long getBuffLoginMoneyGiving() {
		return buffLoginMoneyGiving;
	}

	public void setBuffLoginMoneyGiving(long buffLoginMoneyGiving) {
		this.buffLoginMoneyGiving = buffLoginMoneyGiving;
	}


	public long getBuffMultiplePoint() {
		return buffMultiplePoint;
	}

	public void setBuffMultiplePoint(long buffMultiplePoint) {
		this.buffMultiplePoint = buffMultiplePoint;
	}


	public int getSpeaker() {
		return speaker;
	}

	public void setSpeaker(int speaker) {
		this.speaker = speaker;
	}


	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMoneyGiving() {
		return moneyGiving;
	}

	public void setMoneyGiving(int moneyGiving) {
		this.moneyGiving = moneyGiving;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSkinid() {
		return skinid;
	}

	public void setSkinid(int skinid) {
		this.skinid = skinid;
	}

	public int getLoginMoneyGiving() {
		return loginMoneyGiving;
	}

	public void setLoginMoneyGiving(int loginMoneyGiving) {
		this.loginMoneyGiving = loginMoneyGiving;
	}


	public int getClearPoint() {
		return clearPoint;
	}

	public void setClearPoint(int clearPoint) {
		this.clearPoint = clearPoint;
	}

	public int getClearTimes() {
		return clearTimes;
	}

	public void setClearTimes(int clearTimes) {
		this.clearTimes = clearTimes;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getAllowPurchace() {
		return allowPurchace;
	}

	public void setAllowPurchace(int allowPurchace) {
		this.allowPurchace = allowPurchace;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
}
