package com.chitu.poker.store.msg;

import cn.gecko.broadcast.GeneralResponse;

/**
 * 购买体力DTO
 * @author open
 *
 */
public class BuyStrengthDto extends GeneralResponse {

	private int playerStrength;
	
	private int buyStrengthTimes;
	
	public BuyStrengthDto(int playerStrength, int buyStrengthTimes){
		this.buyStrengthTimes = buyStrengthTimes;
	}

	/**已购买体力次数**/
	public int getBuyStrengthTimes() {
		return buyStrengthTimes;
	}

	public void setBuyStrengthTimes(int buyStrengthTimes) {
		this.buyStrengthTimes = buyStrengthTimes;
	}

	/**玩家当前体力值**/
	public int getPlayerStrength() {
		return playerStrength;
	}

	public void setPlayerStrength(int playerStrength) {
		this.playerStrength = playerStrength;
	}
	
	
}
