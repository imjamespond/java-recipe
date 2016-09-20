package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;


/**
 * 玩家比赛排行
 * @author Administrator
 *
 */
public class ChessPlayerMatchRankMDto extends GeneralResponse {


	private String accountId;
	private String nickname;
	private String prize;

	private int avatar;
	private int gender;

	public ChessPlayerMatchRankMDto() {

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
	 * 奖励
	 * @return
	 */
	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
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









}
