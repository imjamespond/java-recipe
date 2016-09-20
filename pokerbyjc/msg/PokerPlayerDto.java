package com.chitu.poker.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;

import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.model.PokerPlayer;

/**
 * 用户数据Dto
 * 
 * @author open
 * 
 */
public class PokerPlayerDto extends GeneralResponse implements BroadcastMessage {

	private String id;
	private int grade;
	private int exp;
	private int maxExp;
	private String nickname;
	private int money;
	private int point;
	private int strength;
	private int maxStrength;
	private int control;
	private int buyStrengthTimes;

	private int newbieStep;
	private int friendPoint;

	public PokerPlayerDto(PokerPlayer player, boolean self) {
		this.id = String.valueOf(player.id);
		this.nickname = player.nickname;
		this.grade = player.grade;

		StaticPlayerGrade staticGrade = StaticPlayerGrade.get(player.grade);
		this.exp = player.getExp();
		this.maxExp = staticGrade.getUpdateExp();
		this.strength = player.strength;
		this.maxStrength = staticGrade.getMaxStrength();
		this.control = staticGrade.getMaxControl();

		if (self) {
			this.money = player.wealthHolder.getMoney();
			this.point = player.wealthHolder.getPoint();
			this.buyStrengthTimes = player.storeHolder.buyStrengthTimes;
			this.newbieStep = player.newbieStep;
			this.friendPoint = player.friendPoint;
		}
	}

	/** 用户ID */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/** 等级 */
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/** 经验 */
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	/** 昵称 */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/** 金币 */
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	/** 魔法石 */
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	/** 玩家升级经验，经验条最大值 **/
	public int getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	/** 当前体力 **/
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	/** 最大体力 **/
	public int getMaxStrength() {
		return maxStrength;
	}

	public void setMaxStrength(int maxStrength) {
		this.maxStrength = maxStrength;
	}

	/** 统御力 **/
	public int getControl() {
		return control;
	}

	public void setControl(int control) {
		this.control = control;
	}

	/** 已购买体力次数 **/
	public int getBuyStrengthTimes() {
		return buyStrengthTimes;
	}

	public void setBuyStrengthTimes(int buyStrengthTimes) {
		this.buyStrengthTimes = buyStrengthTimes;
	}

	/**
	 * 新手步骤
	 * 
	 * @return
	 */
	public int getNewbieStep() {
		return newbieStep;
	}

	public void setNewbieStep(int newbieStep) {
		this.newbieStep = newbieStep;
	}

	/**
	 * 友情点
	 * @return
	 */
	public int getFriendPoint() {
		return friendPoint;
	}

	public void setFriendPoint(int friendPoint) {
		this.friendPoint = friendPoint;
	}

}
