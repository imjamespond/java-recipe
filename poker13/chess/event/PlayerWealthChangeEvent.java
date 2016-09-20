package com.chitu.chess.event;

import cn.gecko.player.event.PlayerEvent;

public class PlayerWealthChangeEvent extends PlayerEvent {

	private int money;
	private int point;
    private int prestige;
	private int type;

	public PlayerWealthChangeEvent(int money, int point,int prestige) {
		this.money = money;
		this.point = point;
		this.prestige = prestige;
	}

	public PlayerWealthChangeEvent(int money, int point, int prestige, int type) {
		this(money, point,prestige);
		this.type = type;
	}

	public int getMoney() {
		return money;
	}

	public int getPoint() {
		return point;
	}
	
	public int getPrestige(){
		return prestige;
	}

	/**
	 * 变更原因
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
