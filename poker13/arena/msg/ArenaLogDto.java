package com.chitu.poker.arena.msg;

import cn.gecko.broadcast.BroadcastMessage;

import com.chitu.poker.pet.msg.PetDto;

/**
 * 竞技场日志DTO
 * @author open
 *
 */
public class ArenaLogDto implements BroadcastMessage {

	/**日志类型**/
	public enum ArenaLogType {
		/**主动挑战0**/
		Attack,
		/**被挑战1**/
		Defend;
		
		public static ArenaLogType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	private String id;
	
    private String competitorId;
	
	private String competitorName;
	
	private int competitorCapability;
	
	private int competitorGrade;
	
	private PetDto competitorPet;
	
	private int logType;
	
	private boolean win;
	
	private String logTime;
	
	
	/**战报ID**/
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**竞技者ID**/
	public String getCompetitorId() {
		return competitorId;
	}

	public void setCompetitorId(String competitorId) {
		this.competitorId = competitorId;
	}

	/**竞技者名称**/
	public String getCompetitorName() {
		return competitorName;
	}

	public void setCompetitorName(String competitorName) {
		this.competitorName = competitorName;
	}

	/**日志类型 ArenaLogType**/
	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	/**本人是否获胜**/
	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	/**对手战力**/
	public int getCompetitorCapability() {
		return competitorCapability;
	}

	public void setCompetitorCapability(int competitorCapability) {
		this.competitorCapability = competitorCapability;
	}

	/**对手等级**/
	public int getCompetitorGrade() {
		return competitorGrade;
	}

	public void setCompetitorGrade(int competitorGrade) {
		this.competitorGrade = competitorGrade;
	}

	/**对手主宠**/
	public PetDto getCompetitorPet() {
		return competitorPet;
	}

	public void setCompetitorPet(PetDto competitorPet) {
		this.competitorPet = competitorPet;
	}

	/**时间**/
	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	
}
