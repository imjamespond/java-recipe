package com.chitu.chess.msg;

import com.chitu.chess.model.ChessUtils;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;

/*
 * 房间内玩家
 */
public class RoomUserDto extends GeneralResponse implements BroadcastMessage {

	
	private String playerId;	
	private String playerName;
	private String title;
	
	private int position;	
	private int state;
	
	private int gameAmount;
	private int victoryAmount;
	private int avatar;
	private int gender;
	private int money;
	private int point;
	private int counter;
	private int rank;

	/**
	 * 玩家id
	 */
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * 玩家NAME
	 */
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**位置**/
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * 玩家状态 0为空闲,1为准备,2为已发牌,3为已换牌,4为已出牌,5为已发牌断线或退出
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	


	/** 性别 */
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	/** 金币 */
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/** 积分 */
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
		this.title = ChessUtils.point2Title(point);
	}
	
	/** 筹码*/
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	/** 排名*/
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	
	/**头像
	 * @return
	 */
	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	/**游戏总局数
	 * @return
	 */
	public int getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(int gameAmount) {
		this.gameAmount = gameAmount;
	}

	/**胜利总局数
	 * @return
	 */
	public int getVictoryAmount() {
		return victoryAmount;
	}

	public void setVictoryAmount(int victoryAmount) {
		this.victoryAmount = victoryAmount;
	}

	
	/*
	 * 玩家称号*/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
