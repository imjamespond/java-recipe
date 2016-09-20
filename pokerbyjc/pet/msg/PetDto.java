package com.chitu.poker.pet.msg;

import java.util.ArrayList;
import java.util.List;

import cn.gecko.broadcast.GeneralResponse;

public class PetDto extends GeneralResponse{

    private String uniqueId;
	
	/**是否锁定**/
	private boolean locked;
	
	/**是否应战中**/
	private boolean inAction;
	
	/**当前等级经验**/
	private int exp;
	
	/**宠物staticId**/
	private int staticId;
	
	/**牌型技能**/
	private List<ModelSkillDto> modelSkill = new ArrayList<ModelSkillDto>();
	
	/**牌型攻击**/
	private List<ModelAttackDto> modelAttack = new ArrayList<ModelAttackDto>();
	
	private int hp;
	
	/**+HP**/
	private int advanHp;
	
	/**+属性攻击**/
	private int advanHurt;
	
	
	/**宠物uniqueID**/
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**是否锁定**/
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**当前等级经验**/
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	/**牌型技能,元素ModelSkillDto**/
	public List<ModelSkillDto> getModelSkill() {
		return modelSkill;
	}

	public void setModelSkill(List<ModelSkillDto> modelSkill) {
		this.modelSkill = modelSkill;
	}

	/**牌型攻击,元素ModelAttackDto**/
	public List<ModelAttackDto> getModelAttack() {
		return modelAttack;
	}

	public void setModelAttack(List<ModelAttackDto> modelAttack) {
		this.modelAttack = modelAttack;
	}

	/**是否应战中**/
	public boolean isInAction() {
		return inAction;
	}

	public void setInAction(boolean inAction) {
		this.inAction = inAction;
	}

	/**宠物staticId**/
	public int getStaticId() {
		return staticId;
	}

	public void setStaticId(int staticId) {
		this.staticId = staticId;
	}

	/**当前血量**/
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	/**+HP**/
	public int getAdvanHp() {
		return advanHp;
	}

	public void setAdvanHp(int advanHp) {
		this.advanHp = advanHp;
	}

	/**+属性攻击**/
	public int getAdvanHurt() {
		return advanHurt;
	}

	public void setAdvanHurt(int advanHurt) {
		this.advanHurt = advanHurt;
	}

	

	

	
	
	
	
}
