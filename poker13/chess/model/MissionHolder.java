package com.chitu.chess.model;

import java.util.HashMap;
import java.util.Map;

import com.chitu.chess.data.StaticMission;
import com.chitu.chess.msg.ChessPlayerMissionPrizeDto;

public class MissionHolder {


	private static int TYPEVICTORYEACHDAY = 2;
	private static int TYPEGAMEEACHDAY = 1;
	private static int GAMEEACHDAYBEGIN = 1;
	private static int VICTORYEACHDAYBEGIN = 8;
	private PersistChessPlayer persistPlayer;

	public Mission mission1;// 局数任务id
	public Mission mission2;// 胜利局数任务id

	public Map<Integer, Integer> mission;
	public Map<Integer, Integer> achievement;

	public MissionHolder(PersistChessPlayer persistPlayer) {
		this.persistPlayer = persistPlayer;
		
		mission = new HashMap<Integer, Integer>();
		achievement = new HashMap<Integer, Integer>();

		mission1 = new Mission();
		mission2 = new Mission();
		// 重置每日任务
		if (ChessUtils.isToday(persistPlayer.lastLoginTime)) {
			
			persistPlayer.initMission(mission);//从db读取任务
			if (mission.containsKey(TYPEGAMEEACHDAY)) {
				mission1.id = mission.get(TYPEGAMEEACHDAY);//提取 局数任务id
				checkMission(persistPlayer.getGameAmountEachday(),TYPEGAMEEACHDAY);
			}else{
				mission1.id = GAMEEACHDAYBEGIN;//不存在 就初始化
				mission1.state = Mission.State.NOTDONE;
				mission.put(TYPEGAMEEACHDAY, mission1.id);
				persistPlayer.MissionSerialization(mission);
			}
			if (mission.containsKey(TYPEVICTORYEACHDAY)) {
				mission2.id = mission.get(TYPEVICTORYEACHDAY);// 胜利局数任务id
				checkMission(persistPlayer.getVictoryAmountEachday(),TYPEVICTORYEACHDAY);
			}else{
				mission2.id = VICTORYEACHDAYBEGIN;// 局数任务id
				mission2.state = Mission.State.NOTDONE;
				mission.put(TYPEVICTORYEACHDAY, mission2.id);
				persistPlayer.MissionSerialization(mission);
			}
			ChessUtils.chessLog.info("==============读取每日任务===============");
			ChessUtils.chessLog.info("m1:"+mission1.id+"_"+mission1.state);
			ChessUtils.chessLog.info("m2:"+mission2.id+"_"+mission2.state);
		} else {
			// 清零
			persistPlayer.setVictoryAmountEachday(0);
			persistPlayer.setGameAmountEachday(0);
			mission1.id = GAMEEACHDAYBEGIN;// 局数任务id
			mission2.id = VICTORYEACHDAYBEGIN;// 胜利局数任务id
			mission1.state = Mission.State.NOTDONE;
			mission2.state = Mission.State.NOTDONE;
			mission.put(TYPEGAMEEACHDAY, GAMEEACHDAYBEGIN);
			mission.put(TYPEVICTORYEACHDAY, VICTORYEACHDAYBEGIN);
			persistPlayer.MissionSerialization(mission);
			ChessUtils.chessLog.info("==============重置每日任务===============");
			ChessUtils.chessLog.info("m1:"+mission1.id+"_"+mission1.state);
			ChessUtils.chessLog.info("m2:"+mission2.id+"_"+mission2.state);
		}
		// 初始化成就
		persistPlayer.initAchievement(achievement);
	}

	public int checkMission(int num,int type) {
		ChessUtils.chessLog.info("checkMission_" + num);
		
		Mission mTmp = mission1;
		if(type == TYPEVICTORYEACHDAY)
			mTmp = mission2;
		
		if(mTmp.id == 0)
			return num;
		
		StaticMission st = StaticMission.get(mTmp.id);
		// 完成
		if (num >= st.getCondition()) {
			mission.put(type, mTmp.id);
			mTmp.state = Mission.State.DONE;
			return st.getCondition();
		}else{
			mTmp.state = Mission.State.NOTDONE;			
		}
		return num;
	}


	// next mission
	public ChessPlayerMissionPrizeDto setNext(int type) {
		Mission mTmp = mission1;
		if(type == TYPEVICTORYEACHDAY)
			mTmp = mission2;
		if (mTmp.id == 0)
			return null;
		if (mTmp.state == Mission.State.DONE) {
			StaticMission st = StaticMission.get(mTmp.id);
			ChessPlayerMissionPrizeDto mp = new ChessPlayerMissionPrizeDto();
			
			mp.setMoney(st.getMoney());
			mp.setNext(st.getNext());

			mTmp.id = st.getNext();
			mTmp.state = Mission.State.NOTDONE;
			mission.put(type, mTmp.id);
			persistPlayer.MissionSerialization(mission);
			
			//reset
			if(type == 1)
				persistPlayer.setGameAmountEachday(0);
			else
				persistPlayer.setVictoryAmountEachday(0);
			
			return mp;
		}
		return null;
	}

	public void addVictoryAmount() {
		persistPlayer.setVictoryAmount(persistPlayer.getVictoryAmount() + 1);
		persistPlayer.setVictoryAmountEachday(checkMission(persistPlayer.getVictoryAmountEachday()+1,TYPEVICTORYEACHDAY));
		persistPlayer.MissionSerialization(mission);
	}


	public void addGameAmount() {
		persistPlayer.setGameAmount(persistPlayer.getGameAmount() + 1);
		persistPlayer.setGameAmountEachday(checkMission(persistPlayer.getGameAmountEachday()+1,TYPEGAMEEACHDAY));
		persistPlayer.MissionSerialization(mission);
	}



	public void addContinuousVictory() {
		if (persistPlayer.getContinuousVictoryMax() < persistPlayer.getContinuousVictory()) {
			persistPlayer.setContinuousVictoryMax(persistPlayer.getContinuousVictory());
		}
		persistPlayer.setContinuousVictory(persistPlayer.getContinuousVictory() + 1);
	}

	
	public int getGameAmount() {
		return persistPlayer.getGameAmount();
	}	

	public int getVictoryAmount() {
		return persistPlayer.getVictoryAmount();
	}
	
	public int getGameAmountEachDay() {
		return persistPlayer.getGameAmountEachday();
	}	

	public int getVictoryAmountEachDay() {
		return persistPlayer.getVictoryAmountEachday();
	}
	
	public void resetContinuousVictory() {
		persistPlayer.setContinuousVictory(0);
	}


}
