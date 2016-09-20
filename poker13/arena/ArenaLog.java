package com.chitu.poker.arena;

import java.nio.ByteBuffer;

import cn.gecko.commons.utils.SpringUtils;

import com.chitu.poker.arena.msg.ArenaLogDto;
import com.chitu.poker.arena.msg.ArenaLogDto.ArenaLogType;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.service.PokerPlayerManager;

/**
 * 竞技场日志
 * @author open
 *
 */
public class ArenaLog{
	
	public static final byte LOG_VERSION_1 = 1;
	public static final byte LOG_VERSION = LOG_VERSION_1;
	
	public long id;
	
	public long competitorId;
	
	public ArenaLogType logType;
	
	public boolean win;
	
	public long logTime;
	
	
	public void initBuffer(ByteBuffer buffer){
		this.id = buffer.getLong();
		this.competitorId = buffer.getLong();
		this.logType = ArenaLogType.from(buffer.getInt());
		this.win = buffer.getInt()==1?true:false;
		this.logTime = buffer.getLong();
	}
	
	public void toBuffer(ByteBuffer buffer){
		buffer.putLong(this.id);
		buffer.putLong(this.competitorId);
		buffer.putInt(this.logType.ordinal());
		buffer.putInt(this.win?1:0);
		buffer.putLong(this.logTime);
	}
	
	public int byteLength(){
		return 8 + 8 + 4 + 4 + 4 + 8;
	}
	
	public ArenaLogDto toDto(){
		ArenaLogDto dto = new ArenaLogDto();
		dto.setId(String.valueOf(this.id));
		dto.setCompetitorId(String.valueOf(this.competitorId));
		PokerPlayer player = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(competitorId);
		
		dto.setCompetitorId(String.valueOf(competitorId));
		dto.setCompetitorName(player.nickname);
		dto.setCompetitorGrade(player.grade);
		dto.setCompetitorCapability(player.capability);
		Pet pet = player.petHolder.getTeamMainPet();
		dto.setCompetitorPet(pet.toDto());
		
		dto.setLogType(this.logType.ordinal());
		dto.setWin(this.win);
		dto.setLogTime(String.valueOf(this.logTime));
		return dto;
	}
	
}
