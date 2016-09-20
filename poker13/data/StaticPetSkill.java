package com.chitu.poker.data;

import java.util.HashMap;
import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

import com.chitu.poker.battle.HandEvaluator.ModelType;

/**
 * 宠物初始技能表
 * @author open
 *
 */
public class StaticPetSkill {
    /**宠物ID**/
    private int id;
    
	/**技能编号**/
    private int modelValue1;
    
	private int modelValue2;
	
	private int modelValue3;
	
	private int modelValue4;
	
	private int modelValue5;
	
	private int modelValue6;
	
	private int modelValue7;
	
	private int modelValue8;
	
	private int modelValue9;
	
	private int modelValue10;
	
	
    private int advanHp;
	
	private int advanHurt;
	
	public static StaticPetSkill get(int id) {
		return StaticDataManager.getInstance().get(StaticPetSkill.class, id);
	}
	
	/**
	 * 牌型技能
	 * @return
	 */
	public Map<ModelType,Integer> modelSkill(){
		Map<ModelType,Integer> map = new HashMap<ModelType,Integer>();
		if(modelValue1 > 0){
			map.put(ModelType.ONE_PAIR,modelValue1);
		}
		if(modelValue2 > 0){
			map.put(ModelType.TWO_PAIR,modelValue2);
		}
		if(modelValue3 > 0){
			map.put(ModelType.THREE_OF_A_KIND,modelValue3);
		}
		if(modelValue4 > 0){
			map.put(ModelType.STRAIGHT,modelValue4);
		}
		if(modelValue5 > 0){
			map.put(ModelType.FLUSH,modelValue5);
		}
		if(modelValue6 > 0){
			map.put(ModelType.FULL_HOUSE,modelValue6);
		}
		if(modelValue7 > 0){
			map.put(ModelType.FOUR_OF_A_KIND,modelValue7);
		}
		if(modelValue8 > 0){
			map.put(ModelType.STRAIGHT_FLUSH,modelValue8);
		}
		if(modelValue9 > 0){
			map.put(ModelType.ROYAL_FLUSH,modelValue9);
		}
		if(modelValue10 > 0){
			map.put(ModelType.JOKER,modelValue10);
		}
		return map;
	}

	/**petId**/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**牌型技能**/
	public int getModelValue1() {
		return modelValue1;
	}

	public void setModelValue1(int modelValue1) {
		this.modelValue1 = modelValue1;
	}

	/**牌型技能**/
	public int getModelValue2() {
		return modelValue2;
	}

	public void setModelValue2(int modelValue2) {
		this.modelValue2 = modelValue2;
	}

	/**牌型技能**/
	public int getModelValue3() {
		return modelValue3;
	}

	public void setModelValue3(int modelValue3) {
		this.modelValue3 = modelValue3;
	}

	/**牌型技能**/
	public int getModelValue4() {
		return modelValue4;
	}

	public void setModelValue4(int modelValue4) {
		this.modelValue4 = modelValue4;
	}

	/**牌型技能**/
	public int getModelValue5() {
		return modelValue5;
	}

	public void setModelValue5(int modelValue5) {
		this.modelValue5 = modelValue5;
	}

	/**牌型技能**/
	public int getModelValue6() {
		return modelValue6;
	}

	public void setModelValue6(int modelValue6) {
		this.modelValue6 = modelValue6;
	}

	/**牌型技能**/
	public int getModelValue7() {
		return modelValue7;
	}

	public void setModelValue7(int modelValue7) {
		this.modelValue7 = modelValue7;
	}

	/**牌型技能**/
	public int getModelValue8() {
		return modelValue8;
	}

	public void setModelValue8(int modelValue8) {
		this.modelValue8 = modelValue8;
	}

	/**牌型技能**/
	public int getModelValue9() {
		return modelValue9;
	}

	public void setModelValue9(int modelValue9) {
		this.modelValue9 = modelValue9;
	}

	/**牌型技能**/
	public int getModelValue10() {
		return modelValue10;
	}

	public void setModelValue10(int modelValue10) {
		this.modelValue10 = modelValue10;
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
