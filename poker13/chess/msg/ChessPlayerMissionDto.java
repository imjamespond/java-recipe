package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.chess.model.MissionHolder;
import com.chitu.chess.model.Mission.State;


/**
 * @author Administrator
 *
 */
@IncludeEnums({ @IncludeEnum(State.class) })
public class ChessPlayerMissionDto extends GeneralResponse implements BroadcastMessage {


	private int mission1;
	private int mission2;
	
	private int m1State;
	private int m2State;
	
	private int m1Progress;
	private int m2Progress;
	
	

	public ChessPlayerMissionDto(MissionHolder mh) {
		mission1 = mh.mission1.id;
		mission2 = mh.mission2.id;
		
		m1Progress = mh.checkMission(mh.getGameAmountEachDay(),1);
		m2Progress = mh.checkMission(mh.getVictoryAmountEachDay(),2);
		
		m1State = mh.mission1.state.ordinal();
		m2State = mh.mission2.state.ordinal();
	}



	/**
	 * 游戏局数任务id 0没有任务
	 * @return
	 */
	public int getMission1() {
		return mission1;
	}



	public void setMission1(int mission1) {
		this.mission1 = mission1;
	}


	/**
	 * 游戏胜利局数任务id 0没有任务
	 * @return
	 */
	public int getMission2() {
		return mission2;
	}



	public void setMission2(int mission2) {
		this.mission2 = mission2;
	}


	/**
	 * 游戏局数任务状态
	 * @return
	 */
	public int getM1State() {
		return m1State;
	}



	public void setM1State(int m1State) {
		this.m1State = m1State;
	}


	/**
	 * 游戏得利局数任务状态
	 * @return
	 */
	public int getM2State() {
		return m2State;
	}



	public void setM2State(int m2State) {
		this.m2State = m2State;
	}


	/**
	 * 游戏局数任务进度 0未完成 1完成 
	 * @return
	 */
	public int getM1Progress() {
		return m1Progress;
	}



	public void setM1Progress(int m1Progress) {
		this.m1Progress = m1Progress;
	}


	/**
	 * 游戏胜利局数任务进度
	 * @return
	 */
	public int getM2Progress() {
		return m2Progress;
	}



	public void setM2Progress(int m2Progress) {
		this.m2Progress = m2Progress;
	}



}
