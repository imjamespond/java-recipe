package com.chitu.chess.msg;

import java.util.List;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;


/**
 * @author Administrator
 * 
 */
public class ChessRoomDto extends GeneralResponse implements BroadcastMessage {
	
	public ChessRoomDto(List<RoomUserDto> tmpRoomUsers) {

		roomUser = new RoomUserDto[tmpRoomUsers.size()];
		tmpRoomUsers.toArray(roomUser);
	}

	/**
	 * 房间id
	 */
	private String roomId;

	/**
	 * 房间名称
	 */
	private String roomName;
	
	/**
	 * 区id
	 */
	private int districtId;
	
	/**
	 * 房间状态
	 */
	private int state;
	
	
	/**
	 * 房间倍率
	 */
	private int rate;
	
	/**
	 * 比牌时间
	 */
	private int time;
	
	/**
	 * 最少金币
	 */
	private int minMoney;

	
	/**
	 * 每局抽取金币
	 */
	private int money;
	

	/**
	 * 房间类型
	 */
	private int type;
	
	/**
	 * 决赛圈数
	 */
	private int finals;
	

	
	/**
	 * 玩家
	 */
	private RoomUserDto[] roomUser;
	

	/**
	 * 房间id
	 */
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	/**
	 * 房间名称
	 */
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	/**
	 * 玩家 元素为RoomUserDto
	 */
	public RoomUserDto[] getRoomUser() {
		return roomUser;
	}

	public void setRoomUser(RoomUserDto[] roomUser) {
		this.roomUser = roomUser;
	}



	/**
	 * 房间倍率
	 */
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * 出牌时间限制
	 */
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * 最少金币
	 */
	public int getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(int minMoney) {
		this.minMoney = minMoney;
	}

	/**
	 * 房间状态 0是非理牌, 1是理牌
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 每局抽取金币
	 * @return
	 */
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * 房间类型 0普通 1为比赛
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 决赛圈数
	 * @return
	 */
	public int getFinals() {
		return finals;
	}

	public void setFinals(int finals) {
		this.finals = finals;
	}

	/**
	 * 区id
	 */	
	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
	
	
	
}
