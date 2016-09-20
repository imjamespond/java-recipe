package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;

public class BuyItemDto extends GeneralResponse {

	public int money = 0;

	public BuyItemDto(){
		
	}

	/**
	 * 金币变动
	 * @return
	 */
	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money = money;
	}
	
}
