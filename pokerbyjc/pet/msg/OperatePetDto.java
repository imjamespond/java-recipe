package com.chitu.poker.pet.msg;

import cn.gecko.broadcast.GeneralResponse;

public class OperatePetDto extends GeneralResponse {

	private PetDto targetPet;
	
	private String[] usePets;

	/**最终宠物**/
	public PetDto getTargetPet() {
		return targetPet;
	}

	public void setTargetPet(PetDto targetPet) {
		this.targetPet = targetPet;
	}

	/**消耗宠物,元素String**/
	public String[] getUsePets() {
		return usePets;
	}

	public void setUsePets(String[] usePets) {
		this.usePets = usePets;
	}

	
	
}
