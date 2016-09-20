package com.chitu.poker.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;
import cn.gecko.commons.data.StaticDataManager;

import com.chitu.poker.battle.HandEvaluator.ModelType;

/**
 * 宠物属性数据表
 * @author open
 *
 */
@IncludeEnums({ @IncludeEnum(ModelType.class) })
public class StaticPet implements Comparable<StaticPet>,BroadcastMessage{

	/**地型类型**/
	public enum LandType {
		/**水0**/
		Water,
		/**火1**/
		Fire,
		/**木**/
		Wood,
		/**无**/
		NONE
		;
		
		public static LandType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	@Override
	public int compareTo(StaticPet o) {
		if (this.petId == o.petId) {
			return this.grade - o.grade;
		} else {
			return this.petId - o.petId;
		}
	}
	
	
	private int id;
	
	private String name;
	
	private String desc;
	
	private int icon;
	
	private int petId;
	
	private int grade;
	
	private boolean maxGrade;
	
    private int updateExp;
	
	private int totalExp;
	
	private int systemMoney;
	
	private int star;
	
	private int landType;
	
	private int landValue;
	
	private int hp;
	
	private int defend;
	
	private int controlValue;
	
	private int growValue;
	
	private boolean tensei;
	
	private int forgingExp;
	
	private int quality;
	
	private boolean defaultPet;
	

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
	
	public static StaticPet get(int id) {
		return StaticDataManager.getInstance().get(StaticPet.class, id);
	}
	
	public static StaticPet get(int petId,int grade){
		Map<Integer,StaticPet> map = StaticDataManager.getInstance().getMap(StaticPet.class);
		for(StaticPet pet : map.values()){
			if(pet.getPetId() == petId && pet.getGrade() == grade){
				return pet;
			}
		}
		return null;
	}
	
	public static List<StaticPet> getByStar(int star,int grade){
		List<StaticPet> list = new ArrayList<StaticPet>();
		Map<Integer,StaticPet> map = StaticDataManager.getInstance().getMap(StaticPet.class);
		for(StaticPet pet : map.values()){
			if(pet.getStar() == star && pet.getGrade() == grade){
				list.add(pet);
			}
		}
		return list;
	}
	
	public static StaticPet getByTotleExp(int petId,int totleExp){
		Map<Integer,StaticPet> map = StaticDataManager.getInstance().getMap(StaticPet.class);
		List<StaticPet> petIds = new ArrayList<StaticPet>();
		petIds.addAll(map.values());
		Collections.sort(petIds);

		for(StaticPet data : petIds){
			if(data.getTotalExp() > totleExp){
				return data;
			}
		}
		return null;
	}
	
	public static StaticPet getMaxGrade(int petId){
		Map<Integer,StaticPet> map = StaticDataManager.getInstance().getMap(StaticPet.class);
		for(StaticPet pet : map.values()){
			if(pet.getPetId() == petId && pet.isMaxGrade()){
				return pet;
			}
		}
		return null;
	}
	
	/**
	 * 牌型攻击力
	 * @return
	 */
	public Map<ModelType,Integer> modelAttack(int value){
		Map<ModelType,Integer> map = new HashMap<ModelType,Integer>();
		if(modelValue1 > 0){
			map.put(ModelType.ONE_PAIR,modelValue1 + value);
		}
		if(modelValue2 > 0){
			map.put(ModelType.TWO_PAIR,modelValue2 + value);
		}
		if(modelValue3 > 0){
			map.put(ModelType.THREE_OF_A_KIND,modelValue3 + value);
		}
		if(modelValue4 > 0){
			map.put(ModelType.STRAIGHT,modelValue4 + value);
		}
		if(modelValue5 > 0){
			map.put(ModelType.FLUSH,modelValue5 + value);
		}
		if(modelValue6 > 0){
			map.put(ModelType.FULL_HOUSE,modelValue6 + value);
		}
		if(modelValue7 > 0){
			map.put(ModelType.FOUR_OF_A_KIND,modelValue7 + value);
		}
		if(modelValue8 > 0){
			map.put(ModelType.STRAIGHT_FLUSH,modelValue8 + value);
		}
		if(modelValue9 > 0){
			map.put(ModelType.ROYAL_FLUSH,modelValue9 + value);
		}
		if(modelValue10 > 0){
			map.put(ModelType.JOKER,modelValue10 + value);
		}
		return map;
	}
	
	
	/**宠物唯一编号staticId**/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**名称**/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**等级**/
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**升级所需经验**/
	public int getUpdateExp() {
		return updateExp;
	}

	public void setUpdateExp(int updateExp) {
		this.updateExp = updateExp;
	}

	/**升级所需总经验**/
	public int getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	/**星级**/
	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	/**地型类型，参考StaticInstance.LandType系列常量**/
	public int getLandType() {
		return landType;
	}

	public void setLandType(int landType) {
		this.landType = landType;
	}

	/**地型附加攻击值**/
	public int getLandValue() {
		return landValue;
	}

	public void setLandValue(int landValue) {
		this.landValue = landValue;
	}

	/**HP**/
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	/**防御**/
	public int getDefend() {
		return defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	/**统御值**/
	public int getControlValue() {
		return controlValue;
	}

	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	/**成长值**/
	public int getGrowValue() {
		return growValue;
	}

	public void setGrowValue(int growValue) {
		this.growValue = growValue;
	}

	/**能否转生**/
	public boolean isTensei() {
		return tensei;
	}

	public void setTensei(boolean tensei) {
		this.tensei = tensei;
	}
	
	/**图标**/
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**合成供给经验**/
	public int getForgingExp() {
		return forgingExp;
	}

	public void setForgingExp(int forgingExp) {
		this.forgingExp = forgingExp;
	}

	/**描述**/
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**系统回收价格**/
	public int getSystemMoney() {
		return systemMoney;
	}

	public void setSystemMoney(int systemMoney) {
		this.systemMoney = systemMoney;
	}
	
    /**是否默认列表宠物**/
	public boolean isDefaultPet() {
		return defaultPet;
	}

	public void setDefaultPet(boolean defaultPet) {
		this.defaultPet = defaultPet;
	}

	/**宠物petId**/
	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	/**牌型攻击力**/
	public int getModelValue1() {
		return modelValue1;
	}

	public void setModelValue1(int modelValue1) {
		this.modelValue1 = modelValue1;
	}

	/**牌型攻击力**/
	public int getModelValue2() {
		return modelValue2;
	}

	public void setModelValue2(int modelValue2) {
		this.modelValue2 = modelValue2;
	}

	/**牌型攻击力**/
	public int getModelValue3() {
		return modelValue3;
	}

	public void setModelValue3(int modelValue3) {
		this.modelValue3 = modelValue3;
	}

	/**牌型攻击力**/
	public int getModelValue4() {
		return modelValue4;
	}

	public void setModelValue4(int modelValue4) {
		this.modelValue4 = modelValue4;
	}

	/**牌型攻击力**/
	public int getModelValue5() {
		return modelValue5;
	}

	public void setModelValue5(int modelValue5) {
		this.modelValue5 = modelValue5;
	}

	/**牌型攻击力**/
	public int getModelValue6() {
		return modelValue6;
	}

	public void setModelValue6(int modelValue6) {
		this.modelValue6 = modelValue6;
	}

	/**牌型攻击力**/
	public int getModelValue7() {
		return modelValue7;
	}

	public void setModelValue7(int modelValue7) {
		this.modelValue7 = modelValue7;
	}

	/**牌型攻击力**/
	public int getModelValue8() {
		return modelValue8;
	}

	public void setModelValue8(int modelValue8) {
		this.modelValue8 = modelValue8;
	}

	/**牌型攻击力**/
	public int getModelValue9() {
		return modelValue9;
	}

	public void setModelValue9(int modelValue9) {
		this.modelValue9 = modelValue9;
	}

	/**牌型攻击力**/
	public int getModelValue10() {
		return modelValue10;
	}

	public void setModelValue10(int modelValue10) {
		this.modelValue10 = modelValue10;
	}

	/**是否最高等级**/
	public boolean isMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(boolean maxGrade) {
		this.maxGrade = maxGrade;
	}

	/**品质**/
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}
	
	
	
}
