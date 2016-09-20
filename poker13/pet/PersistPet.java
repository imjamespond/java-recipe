package com.chitu.poker.pet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

/**
 * 宠物数据持久化
 * @author open
 *
 */
@Entity
@Table(name = "poker_pet")
@PersistEntity(cache = false)
public class PersistPet extends PersistObject {

	private long id;
	
	private int petMaxCount;
	
	private byte[] pets;
	
	private byte[] teams;
	
	public static PersistPet get(long id) {
		return PersistObject.get(PersistPet.class, id);
	}
	
	public static Map<Long,Pet> initPets(byte[] byteData){
		Map<Long,Pet> pets = new HashMap<Long,Pet>();
		if (byteData == null || byteData.length == 0) {
			return pets;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		byte version = buffer.get();
		if (version == Pet.PET_VERSION_1) {
			int size = buffer.getInt();
			for(int i=0;i<size;i++){
				Pet pet = new Pet();
				pet.initBuffer(buffer);
				pets.put(pet.uniqueId, pet);
			}
		}
		return pets;
	}
	
	public static byte[] petsData(Map<Long,Pet> pets) {
		int length = 1 + 4 ;
		for(Pet pet : pets.values()){
			length += pet.byteLength();
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(Pet.PET_VERSION);
		buffer.putInt(pets.size());
		for(Pet pet : pets.values()){
			pet.toBuffer(buffer);
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}
	
	public static List<PetTeam> initTeams(byte[] byteData){
		List<PetTeam> teams = new ArrayList<PetTeam>();
		if (byteData == null || byteData.length == 0) {
			return teams;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		byte version = buffer.get();
		if (version == PetTeam.PET_TEAM_VERSION_1) {
			int size = buffer.getInt();
			for(int i=0;i<size;i++){
				PetTeam petTeam = new PetTeam();
				petTeam.initBuffer(buffer);
				teams.add(petTeam);
			}
		}
		return teams;
	}
	
	public static byte[] teamsData(List<PetTeam> teams) {
		int length = 1 + 4 ;
		for(PetTeam petTeam : teams){
			length += petTeam.byteLength();
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(PetTeam.PET_TEAM_VERSION);
		buffer.putInt(teams.size());
		for(PetTeam petTeam : teams){
			petTeam.toBuffer(buffer);
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}
	
	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getPets() {
		return pets;
	}

	public void setPets(byte[] pets) {
		this.pets = pets;
	}

	public byte[] getTeams() {
		return teams;
	}

	public void setTeams(byte[] teams) {
		this.teams = teams;
	}

	public int getPetMaxCount() {
		return petMaxCount;
	}

	public void setPetMaxCount(int petMaxCount) {
		this.petMaxCount = petMaxCount;
	}

	
}
