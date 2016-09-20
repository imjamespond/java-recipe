package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;

public class ChessDistrictDto extends GeneralResponse {

	/**
	 * 区名
	 */
	public String name;	
	/**
	 * 玩家数目
	 */
	public int playerNum;
	/**
	 * 区id
	 */
	public int type;
	/**
	 * 最少金币
	 */
	public int minimunMoney;
	/**
	 * 最多金币
	 */
	public int maximunMoney;
	/**
	 * 积分与金币 倍率
	 */
	public int rate;
	
	public ChessDistrictDto(){
		
	}
	/**
	 * 区名
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 玩家数目
	 */
	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	/**
	 * 区id
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 最少金币
	 */
	public int getMinimunMoney() {
		return minimunMoney;
	}

	public void setMinimunMoney(int minimunMoney) {
		this.minimunMoney = minimunMoney;
	}
	/**
	 * 最多金币
	 */	
	public int getMaximunMoney() {
		return maximunMoney;
	}
	public void setMaximunMoney(int maximunMoney) {
		this.maximunMoney = maximunMoney;
	}
	/**
	 * 积分与金币 倍率
	 */
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	
}
