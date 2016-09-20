package com.chitu.chess.payment;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

/**
 * 支付订单
 * @author open
 *
 */
@Entity
@Table(name = "chess_pay_order")
@PersistEntity(cache = false)
public class PersistPayOrder extends PersistObject {

	private long id;
	
	private long playerId;
	
	private String playerName;
	
	private long payTime;
	
	private int payMoney;
	
	private int payType;
	
	private int payStatus;
	
	/**充值手机号,长度11**/
	private String payMob;
	
	/**神州行充值卡序列号,长度50**/
	private String xlh;
	
	/**神州行充值卡刮开密码,长度50**/
	private String mm;
	
	/**平台服务商订单号码,长度5**/
	private String sid;
	
	/**平台回复通知时间**/
	private long notiflyTime;
	
	
	public static PersistPayOrder get(long id) {
		return PersistObject.get(PersistPayOrder.class, id);
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

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public int getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayMob() {
		return payMob;
	}

	public void setPayMob(String payMob) {
		this.payMob = payMob;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public long getNotiflyTime() {
		return notiflyTime;
	}

	public void setNotiflyTime(long notiflyTime) {
		this.notiflyTime = notiflyTime;
	}

	

	
}
