package com.chitu.poker.store.msg;

import java.util.List;

import com.chitu.poker.pet.msg.PetDto;

import cn.gecko.broadcast.GeneralResponse;

/**
 * 抽奖10次,1-5星DTO
 * @author open
 *
 */
public class BuyPetTenTimesDto extends GeneralResponse {

	private List<PetDto> pets;
	
	public BuyPetTenTimesDto(List<PetDto> pets){
		this.pets = pets;
	}
	
	/**宠物,元素PetDto**/
	public List<PetDto> getPets() {
		return pets;
	}

	public void setPets(List<PetDto> pets) {
		this.pets = pets;
	}
	
}
