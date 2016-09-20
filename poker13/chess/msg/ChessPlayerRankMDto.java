package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.chess.model.Mission.State;


/**
 * 玩家金币排行
 * @author Administrator
 *
 */
@IncludeEnums({ @IncludeEnum(State.class) })
public class ChessPlayerRankMDto extends GeneralResponse {


	private String accountId;
	private String nickname;
	private String title;
	private int money;
	private int gender;
	private int avatar;


	public ChessPlayerRankMDto() {

	}

	/**
	 * 账号
	 * @return
	 */
	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * 昵称
	 * @return
	 */
	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 头衔
	 * @return
	 */
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 金币
	 * @return
	 */
	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * 性别
	 * @return
	 */
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * 头像
	 * @return
	 */
	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}








}
