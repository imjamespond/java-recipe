package com.chitu.poker.battle.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.battle.Battle;
import com.chitu.poker.battle.BattleMember;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.msg.PetDto;

/**
 * 对战一方的信息
 * 
 * @author ivan
 * 
 */
public class BattleMemberDto implements BroadcastMessage {

	private String id;

	private String name;

	private String desc;

	private int icon;

	private int currentHp;

	private int maxHp;

	private int power;

	private int resumeHpCount;

	public PokerDto[] pokers = new PokerDto[Battle.MEMBER_POKER_COUNT];

	public PetDto[] pets = new PetDto[Battle.MEMBER_PET_COUNT];

	public boolean hasInvitePet;

	public BattleMemberDto(BattleMember bm) {
		this.id = String.valueOf(bm.id);
		this.name = bm.name;
		this.desc = bm.desc;
		this.icon = bm.icon;
		this.currentHp = bm.currentHp;
		this.maxHp = bm.maxHp;
		this.power = bm.power;
		this.resumeHpCount = bm.resumeHpCount;
		for (int i = 0; i < bm.pokers.length; i++) {
			if (bm.pokers[i] != null)
				pokers[i] = new PokerDto(bm.pokers[i]);
		}
		for (int i = 0; i < bm.pets.size(); i++) {
			Pet pet = bm.pets.get(i);
			if (pet != null)
				pets[i] = pet.toDto();
		}

		hasInvitePet = (bm.inviteMember != null);
	}

	/**
	 * 唯一标识
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 名字
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 描述
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 图标
	 * 
	 * @return
	 */
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	/**
	 * 当前生命值
	 * 
	 * @return
	 */
	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	/**
	 * 最大生命值
	 * 
	 * @return
	 */
	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	/**
	 * 能量值
	 * 
	 * @return
	 */
	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * 恢复HP次数
	 * 
	 * @return
	 */
	public int getResumeHpCount() {
		return resumeHpCount;
	}

	public void setResumeHpCount(int resumeHpCount) {
		this.resumeHpCount = resumeHpCount;
	}

	/**
	 * 手上的牌，PokerDto数组
	 * 
	 * @return
	 */
	public PokerDto[] getPokers() {
		return pokers;
	}

	public void setPokers(PokerDto[] pokers) {
		this.pokers = pokers;
	}

	/**
	 * 宠物列表，PetDto数组
	 * 
	 * @return
	 */
	public PetDto[] getPets() {
		return pets;
	}

	public void setPets(PetDto[] pets) {
		this.pets = pets;
	}

	/**
	 * 是否有邀请宠物，true：pets数组最后一个味邀请的宠物的信息
	 * 
	 * @return
	 */
	public boolean isHasInvitePet() {
		return hasInvitePet;
	}

	public void setHasInvitePet(boolean hasInvitePet) {
		this.hasInvitePet = hasInvitePet;
	}

}
