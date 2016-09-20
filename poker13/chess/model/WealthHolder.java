package com.chitu.chess.model;

import cn.gecko.broadcast.MessageUtils;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.logs.LogUtils;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.SpringUtils;

import com.chitu.chess.event.PlayerWealthChangeEvent;
import com.chitu.chess.msg.WealthNotify;
import com.chitu.chess.service.ChessPlayerManager;

public class WealthHolder {

	private static int moneyLimit = -1;

	private PersistChessPlayer persistPlayer;

	public WealthHolder(PersistChessPlayer persistPlayer) {
		this.persistPlayer = persistPlayer;
	}

	/** 银币 **/
	public int getMoney() {
		return persistPlayer.getMoney();
	}

	/** 金币 **/
	public int getPoint() {
		return persistPlayer.getPoint();
	}

	/** 威望 **/
	public int getPrestige() {
		return persistPlayer.getPrestige();
	}

	/** rmb **/
	public int getRmb() {
		return persistPlayer.getRmb();
	}
	
	private String info() {
		StringBuilder builder = new StringBuilder(100);
		builder.append("[M:");
		builder.append(persistPlayer.getMoney());
		builder.append(",P:");
		builder.append(persistPlayer.getPoint());
		builder.append(",Rmb:");
		builder.append(persistPlayer.getRmb());
		builder.append("]");
		return builder.toString();
	}

	private void log(int money, int point, int rmb, String before, String after, BillType billType, String desc) {
		LogUtils.log("wealth.log", new WealthLog(persistPlayer.getId(), money,
				point, rmb, before, after, billType, desc));
	}

	/**
	 * 增加普通资源
	 * 
	 * @param money
	 * @param rmb
	 * @param population
	 * @param troops
	 * @param billType
	 * @param desc
	 */
	public synchronized void increaseMoney(int money, int point, int rmb, BillType billType, String desc) {
		if (money < 0 || rmb < 0 || point < 0) {
			throw new GeneralException(ChessErrorCodes.WEALTH_NEGATIVE);
		}

		String before = info();
		if (moneyLimit > 0 && (persistPlayer.getMoney() + money) > moneyLimit) {
			money = moneyLimit - persistPlayer.getMoney();
		}
		persistPlayer.setMoney(persistPlayer.getMoney() + money);
		persistPlayer.setPoint(persistPlayer.getPoint() + point);
		persistPlayer.setRmb(persistPlayer.getRmb() + rmb);
        String after = info();
		log(money, point, rmb, before, after, billType, desc);
		update(billType);
	}

	/**
	 * 减少普通资源
	 * 
	 * @param money
	 * @param prestige
	 * @param population
	 * @param troops
	 * @param billType
	 * @param desc
	 */
	public synchronized void decreaseMoney(int money, int point, int rmb, BillType billType, String desc) {
		if (money < 0 || point < 0 || rmb < 0) {
			throw new GeneralException(ChessErrorCodes.WEALTH_NEGATIVE);
		}

		String before = info();
		persistPlayer.setMoney(persistPlayer.getMoney() - money);
		persistPlayer.setPoint(persistPlayer.getPoint() - point);
		persistPlayer.setRmb(persistPlayer.getRmb()- rmb);
        String after = info();
		log(money, point, rmb, before, after, billType, desc);
		update(billType);
	}


	/**
	 * 是否有足够的银币
	 * 
	 * @param needMoney
	 * @return
	 */
	public boolean hasEnoughMoney(int needMoney) {
		return persistPlayer.getMoney() >= needMoney;
	}

	/**
	 * 是否有足够的金币
	 * 
	 * @param needPoint
	 * @return
	 */
	public boolean hasEnoughPoint(int needPoint) {
		return persistPlayer.getPoint() >= needPoint;
	}

	/**
	 * 是否有足够的威望
	 * 
	 * @param needPrestige
	 * @return
	 */
	public boolean hasEnoughPrestige(int needPrestige) {
		return persistPlayer.getPrestige() >= needPrestige;
	}
	
	/**
	 * 是否有足够的rmb
	 * 
	 * @param needPrestige
	 * @return
	 */
	public boolean hasEnoughRmb(int needRmb) {
		return persistPlayer.getRmb() >= needRmb;
	}	
	
	

	public void update(BillType type) {
		updateInterval(type.getId());
	}

	public void update() {
		updateInterval(0);
	}

	private void updateInterval(int type) {
		long playerId = persistPlayer.getId();
		int money = persistPlayer.getMoney();
		int point = persistPlayer.getPoint();
		int rmb = persistPlayer.getRmb();

		ChessPlayerManager playerManager = SpringUtils
				.getBeanOfType(ChessPlayerManager.class);
		ChessPlayer player = playerManager.getOnlinePlayerById(playerId);
		if (player != null) {
			MessageUtils.addMessage(player, new WealthNotify(playerId, money,
					point, rmb, type));
			player.dispatchEvent(new PlayerWealthChangeEvent(money, point,
					rmb, type));
		}
	}

}
