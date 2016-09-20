package com.chitu.poker.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.chitu.poker.battle.HandEvaluator.ModelType;
import com.chitu.poker.data.StaticInstance;
import com.chitu.poker.data.StaticMonster;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.PetTeam;

public class BattleMember {

	public long id;

	public String name;

	public String desc;

	public int icon;

	public int currentHp;

	public int maxHp;

	public int power;

	public int resumeHpCount;

	public Card[] pokers = new Card[Battle.MEMBER_POKER_COUNT];

	public List<Pet> pets = new ArrayList<Pet>(Battle.MEMBER_PET_COUNT);

	public InviteMember inviteMember;

	/** 牌型攻击 **/
	public Map<ModelType, Integer> modelAttack = new HashMap<HandEvaluator.ModelType, Integer>();

	public BattleMember(PokerPlayer player) {
		this.id = player.id;
		this.name = player.nickname;
		PetTeam petTeam = player.petHolder.getTeam();
		for (long petId : petTeam.pet) {
			if (petId <= 0)
				continue;
			Pet pet = player.petHolder.getPet(petId);
			if (pet == null)
				continue;
			onePet(pet);
		}

		currentHp = maxHp;
	}

	public BattleMember(PokerPlayer player, StaticInstance instance, InviteMember inviteMember) {
		this(player);
		this.resumeHpCount = instance.getRsumeHpCount();
		this.inviteMember = inviteMember;
		if (inviteMember != null) {
			onePet(inviteMember.pet);
		}
	}

	public BattleMember(StaticMonster monster) {
		this.id = monster.getId();
		this.name = monster.getName();
		this.desc = monster.getDesc();
		this.icon = monster.getIcon();
		this.resumeHpCount = 0;
		StaticPet spet = StaticPet.get(monster.getPet0());
		if (spet != null) {
			Pet pet = Pet.getInstance(spet);
			onePet(pet);
		}
		spet = StaticPet.get(monster.getPet1());
		if (spet != null) {
			Pet pet = Pet.getInstance(spet);
			onePet(pet);
		}
		spet = StaticPet.get(monster.getPet2());
		if (spet != null) {
			Pet pet = Pet.getInstance(spet);
			onePet(pet);
		}
		spet = StaticPet.get(monster.getPet3());
		if (spet != null) {
			Pet pet = Pet.getInstance(spet);
			onePet(pet);
		}
		spet = StaticPet.get(monster.getPet4());
		if (spet != null) {
			Pet pet = Pet.getInstance(spet);
			onePet(pet);
		}
		currentHp = maxHp;
	}

	private void onePet(Pet pet) {
		pets.add(pet);
		maxHp += pet.maxHp;
		for (Entry<ModelType, Integer> entry : pet.modelAttack.entrySet()) {
			Integer attack = modelAttack.get(entry.getKey());
			if (attack == null)
				attack = 0;
			modelAttack.put(entry.getKey(), attack + entry.getValue());
		}
	}

	public void clearPokers() {
		for (int i = 0; i < pokers.length; i++) {
			pokers[i] = null;
		}
	}

}
