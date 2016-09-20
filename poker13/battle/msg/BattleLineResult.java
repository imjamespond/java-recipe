package com.chitu.poker.battle.msg;

import java.util.List;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.poker.battle.HandEvaluator.ModelType;

/**
 * 单条牌型线的战斗结果
 * 
 * @author ivan
 * 
 */
@IncludeEnums({ @IncludeEnum(ModelType.class) })
public class BattleLineResult implements BroadcastMessage {

	private int lineIndex;

	private int pokersType;

	private int addPower;

	private List<PetAttackResult> petAttacks;

	private boolean dead;

	/**
	 * 牌型线索引
	 * 
	 * @return
	 */
	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * 牌型，参考ModelType系列常量
	 * 
	 * @return
	 */
	public int getPokersType() {
		return pokersType;
	}

	public void setPokersType(int pokersType) {
		this.pokersType = pokersType;
	}

	/**
	 * 本牌型增加的能量值
	 * 
	 * @return
	 */
	public int getAddPower() {
		return addPower;
	}

	public void setAddPower(int addPower) {
		this.addPower = addPower;
	}

	/**
	 * 所有宠物的攻击效果，PetAttackResult数组
	 * 
	 * @return
	 */
	public List<PetAttackResult> getPetAttacks() {
		return petAttacks;
	}

	public void setPetAttacks(List<PetAttackResult> petAttacks) {
		this.petAttacks = petAttacks;
	}

	/**
	 * 伤害是否致死
	 * 
	 * @return
	 */
	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

}
