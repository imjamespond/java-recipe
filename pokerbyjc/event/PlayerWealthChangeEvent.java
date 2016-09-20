package com.chitu.poker.event;

import cn.gecko.player.event.PlayerEvent;

import com.chitu.poker.model.PersistPokerPlayer;

public class PlayerWealthChangeEvent extends PlayerEvent {

	public int lastMoney;
	public int curMoney;
	
	public int lastPoint;
	public int curPoint;
	
	/**变更原因**/
	public int type;
	
	/**
	 * 更改之前
	 * @param persistPlayer
	 */
	public void changeBefore(PersistPokerPlayer persistPlayer){
		this.lastMoney = persistPlayer.getMoney();
		this.lastPoint = persistPlayer.getPoint();
	}

	/**
	 * 更改之后
	 * @param persistPlayer
	 */
	public void changeAfter(PersistPokerPlayer persistPlayer){
		this.curMoney= persistPlayer.getMoney();
		this.curPoint = persistPlayer.getPoint();
	}

}
