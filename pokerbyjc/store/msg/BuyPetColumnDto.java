package com.chitu.poker.store.msg;

import cn.gecko.broadcast.GeneralResponse;

/**
 * 购买宠物栏DTO
 * @author open
 *
 */
public class BuyPetColumnDto extends GeneralResponse {

	private int petMaxCount;
	
	public BuyPetColumnDto(int petMaxCount){
		this.petMaxCount = petMaxCount;
	}

	/**当前宠物栏上限**/
	public int getPetMaxCount() {
		return petMaxCount;
	}

	public void setPetMaxCount(int petMaxCount) {
		this.petMaxCount = petMaxCount;
	}
	
	
}
