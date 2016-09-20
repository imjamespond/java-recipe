package com.chitu.poker.event;

import cn.gecko.player.event.PlayerEvent;

import com.chitu.poker.model.PokerPlayer;

public class PlayerExpChangeEvent extends PlayerEvent {

	public int lastExp;
	public int curExp;

	public int lastGrade;
	public int curGrade;

	/**
	 * 更改之前
	 * @param persistPlayer
	 */
	public void changeBefore(PokerPlayer player){
		this.lastExp = player.getExp();
		this.lastGrade = player.grade;
	}

	/**
	 * 更改之后
	 * @param persistPlayer
	 */
	public void changeAfter(PokerPlayer player){
		this.curExp = player.getExp();
		this.curGrade = player.grade;
	}
}
