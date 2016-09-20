package com.chitu.poker.pet.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.poker.battle.HandEvaluator.ModelType;
import com.chitu.poker.data.StaticSkill;
import com.chitu.poker.skill.msg.SkillDto;

/**
 * 牌型技能
 * @author open
 *
 */
@IncludeEnums({ @IncludeEnum(ModelType.class) })
public class ModelSkillDto implements BroadcastMessage {

	private int modelType;
	
	private SkillDto skill;
	
	public ModelSkillDto(ModelType modelType, StaticSkill staticSkill){
		this.modelType = modelType.ordinal();
		this.skill = staticSkill.toDto();
	}

	/**牌型**/
	public int getModelType() {
		return modelType;
	}

	public void setModelType(int modelType) {
		this.modelType = modelType;
	}

	/**技能**/
	public SkillDto getSkill() {
		return skill;
	}

	public void setSkill(SkillDto skill) {
		this.skill = skill;
	}
	
	
}
