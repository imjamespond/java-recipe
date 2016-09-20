package com.chitu.poker.arena.msg;

import com.chitu.poker.pet.msg.PetDto;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 矿主DTO
 * @author open
 *
 */
public class MinePlayerDto implements BroadcastMessage {

	private String playerId;

	private String nickname;
	
	private int grade;
	
	private PetDto pet;
	
	private int rank;
	
	private int winCount;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**玩家等级**/
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**主宠DTO**/
	public PetDto getPet() {
		return pet;
	}

	public void setPet(PetDto pet) {
		this.pet = pet;
	}

	/**战区排名**/
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	/**今日胜数**/
	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}
	
	
}
