package com.chitu.poker.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.persist.GenericDao;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

import com.chitu.poker.data.StaticArena;

@Entity
@Table(name = "poker_player")
@PersistEntity(cache = false)
public class PersistPokerPlayer extends PersistObject {

	private long id;

	private String accountId;

	private String nickname;

	private int grade;

	private int exp = 0;

	private Timestamp createTime;

	private Timestamp loginTime;

	private int onlineTime;

	private int money;

	private int point;

	private int strength;

	private int newbieStep;

	private int friendPoint;
	
	private int capability;

	public boolean firstLogin = false;

	public static PersistPokerPlayer get(long id) {
		return PersistPokerPlayer.get(PersistPokerPlayer.class, id);
	}

	public static PersistPokerPlayer getByNickname(String nickname) {
		return SpringUtils.getBeanOfType(GenericDao.class).get(PersistPokerPlayer.class, "where nickname=?", nickname);
	}

	public static List<PersistPokerPlayer> findByGrade(int minGrade, int maxGrade, int count) {
		return SpringUtils.getBeanOfType(GenericDao.class).getAll(PersistPokerPlayer.class, count,
				"where grade>=? and grade<=?", minGrade, maxGrade);
	}
	
	public static List<PersistPokerPlayer> getArenaPlayer(StaticArena staticArena) {
		return SpringUtils.getBeanOfType(GenericDao.class).getAll(PersistPokerPlayer.class, 200,
				"where grade>=? and grade<=? and capability>=? and capability<=?", 
				staticArena.getMixGrade(), staticArena.getMaxGrade(), 
				staticArena.getMixCapability(), staticArena.getMaxCapability());
	}

	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getNewbieStep() {
		return newbieStep;
	}

	public void setNewbieStep(int newbieStep) {
		this.newbieStep = newbieStep;
	}

	public int getFriendPoint() {
		return friendPoint;
	}

	public void setFriendPoint(int friendPoint) {
		this.friendPoint = friendPoint;
	}

	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}

	
}
