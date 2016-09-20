package com.chitu.poker.pet.msg;

import java.util.List;

import cn.gecko.broadcast.GeneralResponse;

/**
 * 宠物栏DTO
 * @author open
 *
 */
public class PetColumnDto extends GeneralResponse{

	private List<PetDto> pets;
	
	private int petMaxCount;

	/**宠物,元素PetDto**/
	public List<PetDto> getPets() {
		return pets;
	}

	public void setPets(List<PetDto> pets) {
		this.pets = pets;
	}

	/**宠物栏上限**/
	public int getPetMaxCount() {
		return petMaxCount;
	}

	public void setPetMaxCount(int petMaxCount) {
		this.petMaxCount = petMaxCount;
	}
	
	
}
