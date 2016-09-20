package com.chitu.poker.model;

import com.chitu.poker.event.PlayerWealthChangeEvent;
import com.chitu.poker.msg.WealthNotify;
import com.chitu.poker.service.PokerPlayerManager;

import cn.gecko.broadcast.MessageUtils;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.logs.LogUtils;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.SpringUtils;

public class WealthHolder {

	private static int moneyLimit = -1;
	
	private PersistPokerPlayer persistPlayer;
	
	public WealthHolder(PersistPokerPlayer persistPlayer){
		this.persistPlayer = persistPlayer;
	}
	
	/**银币**/
	public int getMoney(){
		return persistPlayer.getMoney();
	}
	
	/**金币**/
	public int getPoint(){
		return persistPlayer.getPoint();
	}
	
	
	private void logMoney(int money, int before, int after, BillType billType, String desc){
		LogUtils.log("wealth.log", new WealthLog(persistPlayer.getId(), money, before, after, billType, desc));
	}
	
	private void logPoint(int point, int before, int after, BillType billType, String desc){
		LogUtils.log("point.log", new PointLog(persistPlayer.getId(), point, before, after, billType, desc));
	}
	
	/**
	 * 增加银币
	 * @param money
	 * @param billType
	 * @param desc
	 */
	public synchronized void increaseMoney(int money,BillType billType, String desc) {
		if(money <= 0){
			throw new GeneralException(PokerErrorCodes.WEALTH_NEGATIVE);
		}
		PlayerWealthChangeEvent wealthChangeEvent = new PlayerWealthChangeEvent();
		wealthChangeEvent.changeBefore(persistPlayer);
		int before = getMoney();
		
		if (moneyLimit > 0 && (persistPlayer.getMoney() + money) > moneyLimit) {
			money = moneyLimit - persistPlayer.getMoney();
		}
		persistPlayer.setMoney(persistPlayer.getMoney() + money);
		
		wealthChangeEvent.changeAfter(persistPlayer);
		int after = getMoney();
		
		dispatchWealthChangeEvent(wealthChangeEvent);
		logMoney(money,before, after, billType, desc);
		update(billType);
	}
	
	/**
	 * 减少普通资源
	 * @param money
	 * @param billType
	 * @param desc
	 */
	public synchronized void decreaseMoney(int money,BillType billType, String desc) {
		if(money < 0 ){
			throw new GeneralException(PokerErrorCodes.WEALTH_NEGATIVE);
		}
		if(getMoney() < money){
			throw new GeneralException(PokerErrorCodes.MONEY_NOT_ENOUGH);
		}
		
		PlayerWealthChangeEvent wealthChangeEvent = new PlayerWealthChangeEvent();
		wealthChangeEvent.changeBefore(persistPlayer);
		int before = getMoney();
		
		persistPlayer.setMoney(persistPlayer.getMoney() - money);
		
		wealthChangeEvent.changeAfter(persistPlayer);
		int after = getMoney();
		
		dispatchWealthChangeEvent(wealthChangeEvent);
		logMoney(money,before, after, billType, desc);
		update(billType);
	}
	
	/**
	 * 增加金币
	 * 
	 * @param amount
	 *            增加的数值，不能为负数
	 * @param billType
	 *            操作类型
	 * @param desc
	 *            操作描述
	 */
	public synchronized void increasePoint(int amount, BillType billType, String desc) {
		if (amount <= 0){
			throw new GeneralException(PokerErrorCodes.WEALTH_NEGATIVE);
		}
		PlayerWealthChangeEvent wealthChangeEvent = new PlayerWealthChangeEvent();
		wealthChangeEvent.changeBefore(persistPlayer);
		int before = getPoint();
		
		persistPlayer.setPoint(persistPlayer.getPoint() + amount);
		
		wealthChangeEvent.changeAfter(persistPlayer);
		int after = getPoint();
		
		dispatchWealthChangeEvent(wealthChangeEvent);
		logPoint(amount, before, after, billType, desc);
		update(billType);
	}
	
	/**
	 * 减少金币
	 * 
	 * @param amount
	 *            减少的数值，不能为负数
	 * @param billType
	 *            操作类型
	 * @param desc
	 *            操作描述
	 */
	public synchronized void decreasePoint(int amount, BillType billType, String desc) {
		if (amount <= 0){
			throw new GeneralException(PokerErrorCodes.WEALTH_NEGATIVE);
		}
		if (persistPlayer.getPoint() < amount){
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		}
		
		PlayerWealthChangeEvent wealthChangeEvent = new PlayerWealthChangeEvent();
		wealthChangeEvent.changeBefore(persistPlayer);
		int before = getPoint();
		
		persistPlayer.setPoint(persistPlayer.getPoint() - amount);
		
		wealthChangeEvent.changeAfter(persistPlayer);
		int after = getPoint();
		
		dispatchWealthChangeEvent(wealthChangeEvent);
		logPoint(amount, before, after, billType, desc);
		update(billType);
	}
	
    /**
     * 是否有足够的银币
     * @param needMoney
     * @return
     */
    public boolean hasEnoughMoney(int needMoney) {
    	return persistPlayer.getMoney() >= needMoney;
    }
    
    /**
     * 是否有足够的金币
     * @param needPoint
     * @return
     */
    public boolean hasEnoughPoint(int needPoint){
    	return persistPlayer.getPoint() >= needPoint;
    }
    
    private void update(BillType type) {
		updateInterval(type.getId());
	}

	@SuppressWarnings("unused")
	private void update() {
		updateInterval(0);
	}

	private void updateInterval(int type) {
		long playerId = persistPlayer.getId();
		int money = persistPlayer.getMoney();
		int point = persistPlayer.getPoint();

		PokerPlayerManager playerManager = SpringUtils.getBeanOfType(PokerPlayerManager.class);
		PokerPlayer player = playerManager.getOnlinePlayerById(playerId);
		if (player != null) {
			MessageUtils.addMessage(player, new WealthNotify(playerId,money,point,type));
		}
	}
	
	private void dispatchWealthChangeEvent(PlayerWealthChangeEvent event){
		PokerPlayerManager playerManager = SpringUtils.getBeanOfType(PokerPlayerManager.class);
		PokerPlayer player = playerManager.getOnlinePlayerById(persistPlayer.getId());
		if(player != null){
			player.dispatchEvent(event);
		}
	}
    
}
