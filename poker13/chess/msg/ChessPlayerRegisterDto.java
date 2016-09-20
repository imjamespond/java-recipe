package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;


/**
 * @author Administrator
 *
 */
public class ChessPlayerRegisterDto extends GeneralResponse {

	private String accountId;
	private String nickname;
	private String password;
	private int gender;
	//注册金币奖励
	private int money;

	public ChessPlayerRegisterDto() {
	}

	/**
	 * 奖励金币
	 * @return
	 */
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
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
	 * 密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 性别0女 1男
	 * @return
	 */
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
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
}
