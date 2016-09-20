package com.chitu.chess.msg;

import cn.gecko.broadcast.GeneralResponse;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.chess.model.Mission.State;


/**
 * 玩家积分排行
 * @author Administrator
 *
 */
@IncludeEnums({ @IncludeEnum(State.class) })
public class ChessPlayerRankPDto extends GeneralResponse {


	private String accountId;
	private String nickname;
	private String title;
	private int point;
	private int gender;
	private int avatar;

	public ChessPlayerRankPDto() {

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
	 *积分
	 * @return
	 */
	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
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
