package com.chitu.poker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.RandomUtils;
import cn.gecko.player.msg.ListDto;

import com.chitu.poker.arena.ArenaLog;
import com.chitu.poker.arena.msg.ArenaDto;
import com.chitu.poker.arena.msg.ArenaLogDto;
import com.chitu.poker.arena.msg.ArenaLogDto.ArenaLogType;
import com.chitu.poker.arena.msg.MinePlayerDto;
import com.chitu.poker.data.StaticArena;
import com.chitu.poker.data.StaticArenaMine;
import com.chitu.poker.model.PersistPokerPlayer;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.service.PokerPlayerManager;

public class ArenaController extends MultiGeneralController {

	@Autowired
	private PokerPlayerManager playerManager;
	
	
	public ArenaDto arena(){
		PokerPlayer player = playerManager.getRequestPlayer();
		return player.arenaHolder.toDto();
	}
	
	/**
	 * 查看矿主,元素MinePlayerDto
	 * @param mineId
	 * @return
	 */
	public ListDto minePlayer(int mineId){
		StaticArenaMine mine = StaticArenaMine.get(mineId);
		if(mine == null){
			throw new GeneralException(PokerErrorCodes.MINE_IS_NULL);
		}
		PokerPlayer player = playerManager.getRequestPlayer();
		List<MinePlayerDto> dtos = player.arenaHolder.minePlayer(mineId);
		return new ListDto(dtos);
	}
	
	
	/**
	 * 挑战对手
	 * @return
	 */
	public void dare(){
		PokerPlayer player = playerManager.getRequestPlayer();
		//对应战区
		StaticArena staticArena = StaticArena.get(player.grade, player.capability);
		if(staticArena == null){
			throw new GeneralException(PokerErrorCodes.ARENA_IS_NULL);
		}
		if(staticArena.getStrengthLimit() > player.strength){
			throw new GeneralException(PokerErrorCodes.STRENGTH_NOT_ENOUGH);
		}
		//战区对手
		List<PersistPokerPlayer> targets = PersistPokerPlayer.getArenaPlayer(staticArena);
		int index = RandomUtils.nextInt(targets.size());
		PokerPlayer target = playerManager.getAnyPlayerById(targets.get(index).getId());
		
		//挑战
		//TODO
		//
		//
	}
	
	/**
	 * 战报,元素ArenaLogDto
	 * @return
	 */
	public ListDto logList(){
		PokerPlayer player = playerManager.getRequestPlayer();
		List<ArenaLogDto> dtos = player.arenaHolder.logList();
		return new ListDto(dtos);
	}
	
    /**
     * 复仇
     * @param logId
     */
	public void revenge(String logId){
		PokerPlayer player = playerManager.getRequestPlayer();
		ArenaLog log = player.arenaHolder.getLog(Long.valueOf(logId));
		if(log == null){
			throw new GeneralException(PokerErrorCodes.ARENA_LOG_IS_NULL);
		}
		if(log.logType != ArenaLogType.Defend){
			throw new GeneralException(PokerErrorCodes.ARENA_LOG_NOT_DEFEND);
		}
		PokerPlayer target = playerManager.getAnyPlayerById(log.competitorId);
		
		//复仇
		//TODO
		//
		//
	}
	
	
}
