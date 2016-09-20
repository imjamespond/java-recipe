package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;

/**
 * 财富变更通知
 * 
 * @author ivan
 * 
 */
public class WealthNotify implements BroadcastMessage {

	private String id;
	private int money;
	private int point;
    private int rmb;
	private int type;

	public WealthNotify(long playerId,int money,int point,int rmb,int type) {
		this.id = String.valueOf(playerId);
		this.money = money;
		this.point = point;
		this.rmb = rmb;
		this.type = type;
	}

	/**
	 * 玩家ID
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 现有银币
	 * 
	 * @return
	 */
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * 现有金币
	 * 
	 * @return
	 */
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	
    /**
     * 现有威望
     * @return
     */
	public int getRmb() {
		return rmb;
	}

	public void setRmb(int rmb) {
		this.rmb = rmb;
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
