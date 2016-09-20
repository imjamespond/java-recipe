package com.chitu.poker.arena.msg;

import java.util.List;

import cn.gecko.broadcast.GeneralResponse;

/**
 * 竞技场DTO
 * @author open
 *
 */
public class ArenaDto extends GeneralResponse {

	private int todayWin;
	
	private int todayRank;
	
	private int yesterdayRank;
	
    private List<Integer> activeRepeatReward;
	
	private List<Integer> obtainRepeatReward;
	
	private int obtainRankReward;
	
	private String fristRankId;
	
	private String fristRankName;
	
	private int fristRankWinCount;

	/**今日胜数**/
	public int getTodayWin() {
		return todayWin;
	}

	public void setTodayWin(int todayWin) {
		this.todayWin = todayWin;
	}

	/**今日排名**/
	public int getTodayRank() {
		return todayRank;
	}

	public void setTodayRank(int todayRank) {
		this.todayRank = todayRank;
	}

	/**昨日排名**/
	public int getYesterdayRank() {
		return yesterdayRank;
	}

	public void setYesterdayRank(int yesterdayRank) {
		this.yesterdayRank = yesterdayRank;
	}

	/**激活连胜奖励,元素连胜ID**/
	public List<Integer> getActiveRepeatReward() {
		return activeRepeatReward;
	}

	public void setActiveRepeatReward(List<Integer> activeRepeatReward) {
		this.activeRepeatReward = activeRepeatReward;
	}

	/**已领连胜奖励,元素连胜ID**/
	public List<Integer> getObtainRepeatReward() {
		return obtainRepeatReward;
	}

	public void setObtainRepeatReward(List<Integer> obtainRepeatReward) {
		this.obtainRepeatReward = obtainRepeatReward;
	}

	/**排名奖励,0为不能领,大于0为世界排名奖励ID**/
	public int getObtainRankReward() {
		return obtainRankReward;
	}

	public void setObtainRankReward(int obtainRankReward) {
		this.obtainRankReward = obtainRankReward;
	}

	/**头名playerID**/
	public String getFristRankId() {
		return fristRankId;
	}

	public void setFristRankId(String fristRankId) {
		this.fristRankId = fristRankId;
	}

	/**头名名称**/
	public String getFristRankName() {
		return fristRankName;
	}

	public void setFristRankName(String fristRankName) {
		this.fristRankName = fristRankName;
	}

	/**头名今日胜数**/
	public int getFristRankWinCount() {
		return fristRankWinCount;
	}

	public void setFristRankWinCount(int fristRankWinCount) {
		this.fristRankWinCount = fristRankWinCount;
	}


	
	
	
	
	
	
}
