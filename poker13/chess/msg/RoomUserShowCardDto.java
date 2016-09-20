package com.chitu.chess.msg;

import com.chitu.chess.model.ChessRoomPlayer;
import com.chitu.chess.model.ChessUtils;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class RoomUserShowCardDto implements BroadcastMessage {
	
	public String playerId;

	public int[] firstCard;
	public int[] secondCard;	
	public int[] thirdCard;
	
	public int[] firstPoint;
	public int[] secondPoint;
	public int[] thirdPoint;
	
	public int[] firstCreep;
	public int[] secondCreep;
	public int[] thirdCreep;
	
	public int[] special;
	
	public int[] shot;
	
	public int specialType = 0;
	public int point = 0;
	public int money = 0;
	public int position = 0;
	
	public int victoryAmount = 0;
	public int gameAmount = 0;
	public int pointAmount = 0;
	public int moneyAmount = 0;

	private String title;

	public int counter = 0;
	//public int counterAmount = 0;



	public RoomUserShowCardDto(ChessRoomPlayer chessRoomPlayer){
		this.playerId = String.valueOf(chessRoomPlayer.playerId);
		this.firstCard = chessRoomPlayer.playerCardSequence1;
		this.secondCard = chessRoomPlayer.playerCardSequence2;
		this.thirdCard = chessRoomPlayer.playerCardSequence3;
		
		this.firstPoint = chessRoomPlayer.cardScore1;
		this.secondPoint = chessRoomPlayer.cardScore2;
		this.thirdPoint = chessRoomPlayer.cardScore3;
		
		this.firstCreep = chessRoomPlayer.cardCreep1;
		this.secondCreep = chessRoomPlayer.cardCreep2;
		this.thirdCreep = chessRoomPlayer.cardCreep3;
		
		this.special = chessRoomPlayer.cardSpecial;
		
		this.specialType = chessRoomPlayer.cardSpecialType;
		this.point = chessRoomPlayer.cardScoreSum;
		this.money = chessRoomPlayer.cardMoneySum;
		
		this.shot = chessRoomPlayer.shotPosition;

	}
	
	
	/**第一墩牌
	 * @return
	 */
	public int[] getFirstCard() {
		return firstCard;
	}

	public void setFirstCard(int[] firstCard) {
		this.firstCard = firstCard;
	}

	
	/**第二墩牌
	 * @return
	 */
	public int[] getSecondCard() {
		return secondCard;
	}

	public void setSecondCard(int[] secondCard) {
		this.secondCard = secondCard;
	}

	/**第三墩牌
	 * @return
	 */
	public int[] getThirdCard() {
		return thirdCard;
	}

	public void setThirdCard(int[] thirdCard) {
		this.thirdCard = thirdCard;
	}

	/**玩家ID
	 * @return
	 */
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**第一墩分数 int[] firstPoint[0]为对0号玩家的分数
	 * @return
	 */
	public int[] getFirstPoint() {
		return firstPoint;
	}

	public void setFirstPoint(int[] firstPoint) {
		this.firstPoint = firstPoint;
	}

	/**第二墩牌分数 int[]
	 * @return
	 */	
	public int[] getSecondPoint() {
		return secondPoint;
	}

	public void setSecondPoint(int[] secondPoint) {
		this.secondPoint = secondPoint;
	}
	
	/**第三墩分数  int[]
	 * @return
	 */
	public int[] getThirdPoint() {
		return thirdPoint;
	}

	public void setThirdPoint(int[] thirdPoint) {
		this.thirdPoint = thirdPoint;
	}

	
	/**打枪位置int[]
	 * @return
	 */
	public int[] getShot() {
		return shot;
	}

	public void setShot(int[] shot) {
		this.shot = shot;
	}

	/**第一墩怪物牌分数 int[]
	 * @return
	 */
	public int[] getFirstCreep() {
		return firstCreep;
	}

	public void setFirstCreep(int[] firstCreep) {
		this.firstCreep = firstCreep;
	}

	/**第二墩怪物牌分数 int[]
	 * @return
	 */
	public int[] getSecondCreep() {
		return secondCreep;
	}

	public void setSecondCreep(int[] secondCreep) {
		this.secondCreep = secondCreep;
	}

	/**第三墩怪物牌分数 int[]
	 * @return
	 */
	public int[] getThirdCreep() {
		return thirdCreep;
	}

	public void setThirdCreep(int[] thirdCreep) {
		this.thirdCreep = thirdCreep;
	}
	/**玩家特殊牌分数 int[]
	 * @return
	 */	
	public int[] getSpecial() {
		return special;
	}
	public void setSpecial(int[] special) {
		this.special = special;
	}
	/**特殊牌类型
	 * @return
	 */
	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	
	/**玩家积分总数
	 * @return
	 */
	public int getPointAmount() {
		return pointAmount;
	}

	public void setPointAmount(int pointAmount) {
		this.pointAmount = pointAmount;
		this.title = ChessUtils.point2Title(pointAmount);
	}
	
	/**玩家金币总数
	 * @return
	 */	
	public int getMoneyAmount() {
		return moneyAmount;
	}
	public void setMoneyAmount(int money) {
		this.moneyAmount = money;
	}
	
	/**本局获得积分
	 * @return
	 */	
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	/**本局获得金币
	 * @return
	 */	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/**玩家胜利总局数
	 * @return
	 */	
	public int getVictoryAmount() {
		return victoryAmount;
	}

	public void setVictoryAmount(int victoryAmount) {
		this.victoryAmount = victoryAmount;
	}

	/**玩家游戏总局数
	 * @return
	 */	
	public int getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(int gameAmount) {
		this.gameAmount = gameAmount;
	}


	/**玩家出牌的位置
	 * @return
	 */	
	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}

	/*
	 * 玩家称号
	 * */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	/**玩家筹码
	 * @return
	 */	
	public int getCounter() {
		return counter;
	}


	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**玩家总筹码
	 * @return
	 
	public int getCounterAmount() {
		return counterAmount;
	}


	public void setCounterAmount(int counterAmount) {
		this.counterAmount = counterAmount;
	}
	*/	
}
