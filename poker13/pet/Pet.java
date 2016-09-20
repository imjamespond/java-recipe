package com.chitu.poker.pet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gecko.commons.utils.IdUtils;

import com.chitu.poker.battle.HandEvaluator.ModelType;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.data.StaticPetSkill;
import com.chitu.poker.data.StaticSkill;
import com.chitu.poker.pet.msg.ModelAttackDto;
import com.chitu.poker.pet.msg.ModelSkillDto;
import com.chitu.poker.pet.msg.PetDto;

/**
 * 宠物
 * 
 * @author open
 * 
 */
public class Pet {

	public static final byte PET_VERSION_1 = 1;
	public static final byte PET_VERSION = PET_VERSION_1;

	/** +1HP,提升HP10点 **/
	public static final int ADVAN_HP_VALUE = 10;

	/** +1攻击,提升5点攻击 **/
	public static final int ADVAN_ATTACK_VALUE = 5;

	/** +值最大累积值 **/
	public static final int ADVAN_MAX_VALUE = 99;

	/** 唯一ID **/
	public long uniqueId;

	/** 是否锁定 **/
	public boolean locked;

	/** 是否应战中 **/
	public boolean inAction;

	/** 当前等级经验 **/
	public int exp;

	/** 宠物唯一编号staticId **/
	public int staticId;

	/** 牌型技能,value:技能表编号 **/
	public Map<ModelType, Integer> modelSkill;

	/** 牌型攻击 **/
	public Map<ModelType, Integer> modelAttack;

	/** +HP **/
	public int advanHp;

	/** +攻击 **/
	public int advanHurt;

	public int maxHp;

	public int hp;

	public static Pet getInstance(StaticPet staticData) {
		Pet pet = new Pet();
		pet.uniqueId = IdUtils.generateLongId();
		pet.staticId = staticData.getId();

		StaticPetSkill staticPetSkill = StaticPetSkill.get(staticData.getPetId());
		if (staticPetSkill != null) {
			pet.advanHp = staticPetSkill.getAdvanHp();
			pet.advanHurt = staticPetSkill.getAdvanHurt();
			pet.modelSkill = staticPetSkill.modelSkill();
		} else
			pet.modelSkill = new HashMap<ModelType, Integer>(0);

		pet.maxHp = staticData.getHp() + pet.advanHp * ADVAN_HP_VALUE;
		pet.hp = pet.maxHp;
		pet.modelAttack = staticData.modelAttack(pet.advanHurt * ADVAN_ATTACK_VALUE);
		return pet;
	}

	public void initBuffer(ByteBuffer buffer) {
		this.uniqueId = buffer.getLong();
		this.locked = buffer.getInt() == 1 ? true : false;
		this.inAction = buffer.getInt() == 1 ? true : false;
		this.exp = buffer.getInt();
		this.staticId = buffer.getInt();
		this.advanHp = buffer.getInt();
		this.advanHurt = buffer.getInt();
		this.hp = buffer.getInt();

		int size = buffer.getInt();
		this.modelSkill = new HashMap<ModelType, Integer>();
		for (int i = 0; i < size; i++) {
			ModelType modelType = ModelType.from(buffer.getInt());
			int skillId = buffer.getInt();
			this.modelSkill.put(modelType, skillId);
		}

		StaticPet staticData = StaticPet.get(this.staticId);
		this.modelAttack = staticData.modelAttack(this.advanHurt * ADVAN_ATTACK_VALUE);
		this.maxHp = staticData.getHp() + this.advanHp * ADVAN_HP_VALUE;
	}

	public void toBuffer(ByteBuffer buffer) {
		buffer.putLong(this.uniqueId);
		buffer.putInt(this.locked ? 1 : 0);
		buffer.putInt(this.inAction ? 1 : 0);
		buffer.putInt(this.exp);
		buffer.putInt(this.staticId);
		buffer.putInt(this.advanHp);
		buffer.putInt(this.advanHurt);
		buffer.putInt(this.hp);

		buffer.putInt(this.modelSkill.size());
		for (Map.Entry<ModelType, Integer> entry : this.modelSkill.entrySet()) {
			buffer.putInt(entry.getKey().ordinal());
			buffer.putInt(entry.getValue());
		}
	}

	public int byteLength() {
		return 8 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + this.modelSkill.size() * (4 + 4);
	}

	public PetDto toDto() {
		PetDto dto = new PetDto();
		dto.setUniqueId(String.valueOf(this.uniqueId));
		dto.setLocked(this.locked);
		dto.setExp(this.exp);
		dto.setStaticId(this.staticId);
		dto.setInAction(this.inAction);
		dto.setHp(this.hp);
		dto.setAdvanHp(this.advanHp);
		dto.setAdvanHurt(this.advanHurt);

		List<ModelSkillDto> modelSkill = new ArrayList<ModelSkillDto>();
		for (Map.Entry<ModelType, Integer> entry : this.modelSkill.entrySet()) {
			ModelType modelType = entry.getKey();
			StaticSkill staticSkill = StaticSkill.get(entry.getValue());
			ModelSkillDto modelSkillDto = new ModelSkillDto(modelType, staticSkill);
			modelSkill.add(modelSkillDto);
		}
		dto.setModelSkill(modelSkill);

		List<ModelAttackDto> modelAttack = new ArrayList<ModelAttackDto>();
		for (Map.Entry<ModelType, Integer> entry : this.modelAttack.entrySet()) {
			ModelAttackDto modelAttackDto = new ModelAttackDto(entry.getKey(), entry.getValue());
			modelAttack.add(modelAttackDto);
		}
		dto.setModelAttack(modelAttack);

		return dto;
	}

	/**
	 * 增加经验
	 * 
	 * @param exp
	 */
	public void incExp(int exp) {
		if (exp <= 0) {
			return;
		}

		StaticPet staticPet = StaticPet.get(this.staticId);
		StaticPet nextPet = StaticPet.get(staticPet.getPetId(), staticPet.getGrade() + 1);
		if (nextPet == null) {
			return;
		}

		int totleExp = staticPet.getTotalExp() - staticPet.getUpdateExp() + this.exp + exp;
		StaticPet targetPet = StaticPet.getByTotleExp(staticPet.getPetId(), totleExp);
		if (targetPet == null) {
			this.staticId = StaticPet.getMaxGrade(staticPet.getPetId()).getId();
			this.exp = 0;
		} else {
			this.staticId = targetPet.getId();
			this.exp = totleExp - (targetPet.getTotalExp() - targetPet.getUpdateExp());
		}
	}

}
