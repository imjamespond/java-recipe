package com.chitu.poker.pet.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.poker.battle.HandEvaluator.ModelType;

/**
 * 牌型攻击
 * @author open
 *
 */
@IncludeEnums({ @IncludeEnum(ModelType.class) })
public class ModelAttackDto implements BroadcastMessage {

	private int modelType;
	
	private int attackValue;
	
	public ModelAttackDto(ModelType modelType,int attackValue){
		this.modelType = modelType.ordinal();
		this.attackValue = attackValue;
	}

	/**牌型**/
	public int getModelType() {
		return modelType;
	}

	public void setModelType(int modelType) {
		this.modelType = modelType;
	}

	/**攻击值**/
	public int getAttackValue() {
		return attackValue;
	}

	public void setAttackValue(int attackValue) {
		this.attackValue = attackValue;
	}
	
	
}
