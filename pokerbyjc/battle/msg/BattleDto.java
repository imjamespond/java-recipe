package com.chitu.poker.battle.msg;

import java.util.ArrayList;
import java.util.List;

import com.chitu.poker.data.StaticBattle;
import com.chitu.poker.data.StaticMonster;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.msg.PetDto;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 关卡信息
 * 
 * @author ivan
 * 
 */
public class BattleDto implements BroadcastMessage {

	private StaticBattle staticBattle;

	private boolean pass;

	private List<PetDto> pets = new ArrayList<PetDto>(5);

	public BattleDto(StaticBattle staticBattle, boolean pass) {
		this.staticBattle = staticBattle;
		this.pass = pass;
		if (staticBattle.getMonsterId() <= 0)
			return;
		StaticMonster moster = StaticMonster.get(staticBattle.getMonsterId());
		if (moster == null)
			return;
		if (moster.getPet0() > 0) {
			StaticPet spet = StaticPet.get(moster.getPet0());
			if (spet != null)
				pets.add(Pet.getInstance(spet).toDto());
		}
		if (moster.getPet1() > 0) {
			StaticPet spet = StaticPet.get(moster.getPet1());
			if (spet != null)
				pets.add(Pet.getInstance(spet).toDto());
		}
		if (moster.getPet2() > 0) {
			StaticPet spet = StaticPet.get(moster.getPet2());
			if (spet != null)
				pets.add(Pet.getInstance(spet).toDto());
		}
		if (moster.getPet3() > 0) {
			StaticPet spet = StaticPet.get(moster.getPet3());
			if (spet != null)
				pets.add(Pet.getInstance(spet).toDto());
		}
		if (moster.getPet4() > 0) {
			StaticPet spet = StaticPet.get(moster.getPet4());
			if (spet != null)
				pets.add(Pet.getInstance(spet).toDto());
		}
	}

	/**
	 * 关卡静态数据
	 * 
	 * @return
	 */
	public StaticBattle getStaticBattle() {
		return staticBattle;
	}

	public void setStaticBattle(StaticBattle staticBattle) {
		this.staticBattle = staticBattle;
	}

	/**
	 * 是否已经通关
	 * 
	 * @return
	 */
	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	/**
	 * 敌方的宠物列表，PetDto数组
	 * 
	 * @return
	 */
	public List<PetDto> getPets() {
		return pets;
	}

	public void setPets(List<PetDto> pets) {
		this.pets = pets;
	}

}
