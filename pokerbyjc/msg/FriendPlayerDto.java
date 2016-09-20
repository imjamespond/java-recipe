package com.chitu.poker.msg;

import cn.gecko.broadcast.GeneralResponse;

import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.msg.PetDto;

public class FriendPlayerDto extends GeneralResponse{

	private String id;

	private String nickname;
	
	private int grade;
	
	private PetDto pet;

	public FriendPlayerDto(PokerPlayer player) {
		this.id = String.valueOf(player.id);
		this.nickname = player.nickname;
		this.grade = player.grade;
		
		Pet pet = player.petHolder.getTeamMainPet();
		if(pet != null){
			this.pet = pet.toDto();
		}
	}

	/**
	 * 好友的用户ID
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
	 * 好友的用户昵称
	 * 
	 * @return
	 */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**等级**/
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**好友主宠**/
	public PetDto getPet() {
		return pet;
	}

	public void setPet(PetDto pet) {
		this.pet = pet;
	}
}
