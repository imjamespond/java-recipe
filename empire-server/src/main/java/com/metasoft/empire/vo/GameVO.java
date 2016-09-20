package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("游戏分配消息")
@EventAnno(desc = "", name = "event.game")
public class GameVO {
	private PlayerVO player1= new PlayerVO();
	private PlayerVO player0= new PlayerVO();
	private int turn;
	private int map;
	private int state;
	
	public PlayerVO getPlayer1() {
		return player1;
	}
	public void setPlayer1(PlayerVO player1) {
		this.player1 = player1;
	}
	public PlayerVO getPlayer0() {
		return player0;
	}
	public void setPlayer0(PlayerVO player0) {
		this.player0 = player0;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

}
