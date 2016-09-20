package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 单个宠物的攻击效果
 * 
 * @author ivan
 * 
 */
public class PetAttackResult implements BroadcastMessage{

	private String petId;

	private int attackLife;

	public PetAttackResult(long petId, int attackLife) {
		this.petId = String.valueOf(petId);
		this.attackLife = attackLife;
	}

	/**
	 * 宠物ID
	 * 
	 * @return
	 */
	public String getPetId() {
		return petId;
	}

	public void setPetId(String petId) {
		this.petId = petId;
	}

	/**
	 * 攻击伤害值
	 * 
	 * @return
	 */
	public int getAttackLife() {
		return attackLife;
	}

	public void setAttackLife(int attackLife) {
		this.attackLife = attackLife;
	}

}
