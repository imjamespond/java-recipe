package com.chitu.poker.arena;

import cn.gecko.commons.utils.SpringUtils;

import com.chitu.poker.arena.msg.MinePlayerDto;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.service.PokerPlayerManager;

/**
 * 矿主
 * @author open
 *
 */
public class MinePlayerUnit {

	public long playerId;
	
	public int mineId;
	
	public int rank;
	
	public int winCount;
	
	
	public MinePlayerDto toDto(){
		PokerPlayer player = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(playerId);
		MinePlayerDto dto = new MinePlayerDto();
		dto.setPlayerId(String.valueOf(playerId));
		dto.setNickname(player.nickname);
		dto.setRank(rank);
		dto.setGrade(player.grade);
		dto.setWinCount(winCount);
		
		Pet pet = player.petHolder.getTeamMainPet();
		dto.setPet(pet.toDto());
		return dto;
	}
	
}
