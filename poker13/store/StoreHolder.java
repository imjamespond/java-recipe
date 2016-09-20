package com.chitu.poker.store;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import cn.gecko.commons.utils.RandomUtils;

import com.chitu.poker.data.StaticPet;


public class StoreHolder {
	
	/**购买宠物次数累积到幸运值则发大奖**/
	public static final int BUY_PET_LUCK_TIMES = 10;
	
	/**单次购买宠物所需魔石**/
	public static final int BUY_PET_ONE_TIMES_NEED_POINT = 10;
	
	/**10次购买宠物所需魔石**/
	public static final int BUY_PET_TEN_TIMES_NEED_POINT = 85;

	private long playerId;
	
	/**已购买体力次数**/
	public int buyStrengthTimes;
	
	/**购买体力次数上限**/
	private int buyStrengthMaxTimes = 10;
	
	/**购买宠物次数**/
	private int buyPetTimes;
	
	private int today;
	
	private PersistStore persist;
	
	private boolean update;

	private boolean needSave;
	
	
	public StoreHolder(long playerId){
		this.playerId = playerId;
	}
	
	/**持久化**/
	public void persist() {
		if (persist == null) {
			return;
		}

		persist.setBuyStrengthMaxTimes(buyStrengthMaxTimes);
		persist.setBuyStrengthTimes(buyStrengthTimes);
		persist.setBuyPetTimes(buyPetTimes);
		persist.setToday(today);

		if (needSave) {
			persist.save();
			needSave = false;
		}
		else if (update) {
			persist.update();
			update = false;
		}
	}
	
	/**角色下线**/
	public void destroy() {
		persist = null;
		playerId = 0;
	}
	
	private void init() {
		if (persist != null) {
			return;
		}
		
		persist = PersistStore.get(playerId);
		if (persist != null) {
			
			this.buyStrengthMaxTimes = persist.getBuyStrengthMaxTimes();
			this.buyStrengthTimes = persist.getBuyStrengthTimes();
			this.buyPetTimes = persist.getBuyPetTimes();
			this.today = persist.getToday();
			
		} else {
			needSave = true;
			persist = new PersistStore();
			persist.setId(this.playerId);
		}
	}
	
	public void setPersistUpdate(boolean update) {
		this.update = update;
	}
	
	/**
	 * 每天刷新，挑战次数与领奖次数重置
	 */
	private void refresh(){
		int curDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		if (this.today != curDate) {
			this.today = curDate;
			this.buyStrengthTimes = 0;
		}
	}
	
	/**
	 * 累加购买体力次数
	 */
	public int incBuyStrengthTimes(){
		init();
		refresh();
		this.buyStrengthTimes++;
		this.update = true;
		return this.buyStrengthTimes;
	}
	
	/**
	 * 可否购买体力
	 * @return
	 */
	public boolean canBuyStrength(){
		init();
		refresh();
		return this.buyStrengthMaxTimes > this.buyStrengthTimes;
	}
	
	/**
	 * 购买体力所需魔石
	 * @return
	 */
	public int buyStrengthNeedPoint(){
		init();
		refresh();
		
		int needPoint = 0;
		switch (this.buyStrengthTimes) {
		case 0:
			needPoint = 10;
			break;
		case 1:
			needPoint = 20;
			break;
		case 2:
			needPoint = 30;
			break;
		case 3:
			needPoint = 50;
			break;
		case 4:
			needPoint = 100;
			break;
		case 5:
			needPoint = 150;
			break;
		default:
			needPoint = 200;
			break;
		}
		
		return needPoint;
	}
	
	/**
	 * 累加购买宠物次数
	 */
	public void incBuyPetTimes(){
		init();
		refresh();
		this.buyPetTimes++;
		this.update = true;
	}
	
	/**
	 * 随机得到宠物,逢10得到高级宠
	 * @param mixStar
	 * @param maxStar
	 * @return
	 */
	public StaticPet randomPet(int mixStar,int maxStar){
		int limit = 0;
		int[] randomHit = new int[]{100000,50000,30000,15000,4000,1000};
		randomHit = ArrayUtils.subarray(randomHit, mixStar, maxStar+1);
		for(int hit:randomHit){
			limit += hit;
		}
		
		int star = maxStar;
		if(this.buyPetTimes % BUY_PET_LUCK_TIMES != 0){
			star = RandomUtils.randomHit(limit,randomHit) + mixStar;
		}
		
		List<StaticPet> datas = StaticPet.getByStar(star,1);
        int index = RandomUtils.nextInt(datas.size());
		StaticPet staticPet = datas.get(index);
		return staticPet;
	}
	
	
	
	
	
}
