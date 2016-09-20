package com.chitu.chess.msg;

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
public class RoomUserShowCardReplayDto implements BroadcastMessage {
	
	public String playerId;

	public int[] firstCard;
	public int[] secondCard;	
	public int[] thirdCard;
	
	public int specialType = 0;
	public int point = 0;
	public int money = 0;

	private String nickname;

	public RoomUserShowCardReplayDto(){

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


	/**特殊牌类型
	 * @return
	 */
	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
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

	/*
	 * 玩家昵称
	 * */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
