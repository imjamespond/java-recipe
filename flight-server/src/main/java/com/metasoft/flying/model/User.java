package com.metasoft.flying.model;

import java.util.Calendar;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.model.job.LoggerJob;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.util.EpochUtil;
import com.metasoft.flying.vo.UserGiftVO;
import com.metasoft.flying.service.RankService;
import com.metasoft.flying.service.UserDataService;
import com.metasoft.flying.service.UserRankService;
import com.metasoft.flying.service.UserWealthService;
import com.metasoft.flying.service.common.JobService;
import com.metasoft.flying.service.common.SpringService;

public class User {

	private static final Logger log = LoggerFactory.getLogger(User.class);

	private long id;
	//private long appleBegin;

	//private String ip;
	private String group;

	private BaseConnection conn;

	//private boolean micophone;
	public boolean updateUser;
	public boolean updateRank;
	public boolean updateData;

	private Map<Integer, UserFashion> fashionMap;// 时装类型如1为上装,2为下装...,时装细节
	private Map<Long, UserFollow> followingMap;// 玩家关注人的map
	private Map<Integer, UserItem> itemMap;
	private List<UserGiftVO> giftList;
	private Deque<UserMatch> matchDeq;
	private int[] upgrade;

	// persist obj
	private UserPersist userPersist;
	private UserRankPersist userRankPersist;
	private UserDataPersist userDataPersist;
	private UserWealthPersist userWealthPersist;

	public long pveEpoch;
	public int pveNum;
	//public int pveLevel;

	/**
	 * Created by UserService::getAnyPlayerById()
	 * 
	 */
	public User() {
		updateUser = false;
		updateRank = false;
		updateData = false;
	}

	/**
	 * 加苹果
	 * 
	 * @param apple
	 * @param cause
	 
	public synchronized void addApple(int apple, String cause){
		needUpdate = true;
		//log.info("add apple uid:{}, cur:{}, num:{}, desc:{}", id, userPersist.getAppleprize(), apple, cause);
		
		userPersist.setApple(userPersist.getApple()+apple);
		//userPersist.setAppleprize(userPersist.getAppleprize()+apple);
		
		UserRankPersist userRank = getUserRankPersistByWeek();	
		userRank.setApple_in(userRank.getApple_in() + apple);
	}*/

	/**
	 * 加宝石
	 * 
	 * @param Gems
	 * @param cause
	 */
	public synchronized void addGems(int Gems, String cause) {
		UserWealthPersist userWealth = getUserWealthPersist();
		int preGems = userWealth.getGems();
		
		// db operation
		UserWealthService service = SpringService.getBean(UserWealthService.class);
		if (service.addGems(userWealth, Gems) == 0) {
			log.error("add gems failed: {}",cause);
		}
		
		//update data
		//FIXME
		
		// log queue
		String causeFormat = String.format("增加钻石 :%s, 原:%d, 现:%d, 描述:%s", userPersist.getUsername(), preGems, userWealth.getGems(), cause);
		log.info(causeFormat);
	}

	/**
	 * 消费宝石
	 * 
	 * @param Gems
	 * @param cause
	 */
	public synchronized void reduceGems(int needGems, String cause) throws GeneralException {
		UserWealthPersist userWealth = getUserWealthPersist();
		int preGems = userWealth.getGems();
		
		// db operation
		UserWealthService service = SpringService.getBean(UserWealthService.class);
		if (service.reduceGems(userWealth, needGems) == 0) {
			log.error("reduce gems failed: {}",cause);
			throw new GeneralException(0, "insufficient.Gems");
		}
		
		//update data
//		UserService userService = SpringService.getBean(UserService.class);
//		try {
//			userService.getUserAndUpdate(id);
//		} catch (GeneralException e) {
//			e.printStackTrace();
//		}
		//userPersist.setGems(preGems-needGems);
		// log queue
		String causeFormat = String.format("减少钻石 :%s, 原:%d, 现:%d, 描述:%s", userPersist.getUsername(), preGems, userWealth.getGems(), cause);
		log.info(causeFormat);
/*		ExchangeToWebService service = SpringService.getBean(ExchangeToWebService.class);
		BaseExchange.Builder bbuilder = BaseExchange.newBuilder();
		GemExchange.Builder gbuilder = GemExchange.newBuilder();
		bbuilder.setType(ExchangeToWebService.CMD_GEM_EXCHANGE);
		bbuilder.setExtension(GemExchange.gemExchange, gbuilder.build());
		service.put(bbuilder.build());*/	
		JobService jobService = SpringService.getBean(JobService.class);
		jobService.produce(new LoggerJob(-needGems, userWealth.getGems(), GeneralConstant.TYPE_GEM, id, cause));
	}

	/**
	 * 加分
	 * 
	 * @param score
	 */
	public synchronized void addScore(int score) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		userData.setScore(userData.getScore() + score);
	}

	/**
	 * 加贡献
	 * 
	 * @param c
	 */
	public synchronized void addContribution(int c) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		userData.setContribute(userData.getContribute() + c);
		log.info("contri:{} uid:{}",userData.getContribute(),id);
		UserRankPersist userRank = getUserRankPersistByWeek();	
		userRank.setContribute(userRank.getContribute() + c);
	}

	/**
	 * 加魅力
	 * 
	 * @param c
	 */
	public synchronized void addCharm(int c) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		userData.setCharm(userData.getCharm() + c);
		log.info("charm:{} uid:{}",userData.getCharm(),id);
		UserRankPersist userRank = getUserRankPersistByWeek();	
		userRank.setCharm(userRank.getCharm() + c);
	}
	
	/**
	 * 加金币
	 * 
	 * @param val
	 */
	public synchronized void addGold(int val) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		userData.setGold(userData.getGold() + val);
		log.info("gold:{} uid:{}",userData.getGold(),id);
	}
	
	/**
	 * 扣金币
	 * 
	 * @param val
	 * @throws GeneralException 
	 */
	public synchronized void reduceGold(int val) throws GeneralException {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		if(userData.getGold()<val){
			throw new GeneralException(0, "insufficient.gold");
		}
		userData.setGold(userData.getGold() - val);
		log.info("reduce gold:{} uid:{}",userData.getGold(),id);
	}

	/**
	 * 玫瑰改动
	 * 
	 * @param rose
	 * @return
	 */
	public synchronized void addRose(int rose, String cause) {
		UserWealthPersist userWealth = getUserWealthPersist();
		int preRose = userWealth.getRose();		
		
		// db operation
		UserWealthService service = SpringService.getBean(UserWealthService.class);
		if (service.addRose(id, rose) == 0) {
			log.warn("add rose failed: []",cause);
		}
		userWealth.setRose(preRose+rose);
		// log queue
		String causeFormat = String.format("增加玫瑰 :%s, 原:%d, 现:%d, 描述:%s", userPersist.getUsername(), preRose, userWealth.getRose(), cause);
		log.info(causeFormat);
		JobService jobService = SpringService.getBean(JobService.class);
		jobService.produce(new LoggerJob(rose, userWealth.getRose(), GeneralConstant.TYPE_ROSE, id, cause));
	}

	public synchronized void reduceRose(int needRose, String cause) throws GeneralException {	
		UserWealthPersist userWealth = getUserWealthPersist();
		int preRose = userWealth.getRose();		
	
		// db operation
		UserWealthService service = SpringService.getBean(UserWealthService.class);
		if (service.reduceRose(id, needRose) == 0) {
			log.warn("reduceRose rose failed: {}",cause);
			throw new GeneralException(0, "insufficient.rose");
		}
		
		//update data
		//FIXME
		
		// log queue
		String causeFormat = String.format("扣减玫瑰 :%s, 原:%d, 现:%d, 描述:%s", userPersist.getUsername(), preRose, userWealth.getRose(), cause);
		log.info(causeFormat);
		JobService jobService = SpringService.getBean(JobService.class);
		jobService.produce(new LoggerJob(-needRose, userWealth.getRose(), GeneralConstant.TYPE_ROSE, id, cause));

	}
	/**
	 * 比赛兑换积分
	 * 
	 * @param rose
	 * @return
	 */
	public synchronized void addCredit(int s, String cause) {	
		UserWealthPersist userWealth = getUserWealthPersist();
		// db operation
		UserWealthService service = SpringService.getBean(UserWealthService.class);
		if (service.addCredit(userWealth, s) == 0) {
			log.warn("add credit failed: []",cause);
		}

		// log queue
		String causeFormat = String.format("增加比赛兑换积分 :%s, 当前:%d, 描述:%s", userPersist.getUsername(), userWealth.getCredit(), cause);
		log.info(causeFormat);
		JobService jobService = SpringService.getBean(JobService.class);
		jobService.produce(new LoggerJob(s, userWealth.getCredit(), GeneralConstant.TYPE_CREDIT, id, cause));
	}
	/**
	 * 加道具
	 * 
	 * @param itemId
	 * @param num
	 * @throws GeneralException
	 */
	public synchronized UserItem addItem(int itemId, int num, String cause) throws GeneralException {
		updateData = true;
		Map<Integer, UserItem> itemMap = getItemMap();
		UserItem item = itemMap.get(itemId);
		if (null == item) {
			if (num < 0) {
				throw new GeneralException(0, "insufficient.item");
			}		
			item = new UserItem(itemId, num);
			itemMap.put(itemId, item);
		} else {
			int curNum = item.getItemNum() + num;
			if (curNum < 0) {
				throw new GeneralException(0, "insufficient.item");
			}
			item.setItemNum(curNum);
		}
		
		item.setItemTime(System.currentTimeMillis());
		log.info("道具:{}, num:{}, total:{}, uid:{}, cause:{}", itemId, num, item.getItemNum(), id, cause);
		return item;
	}
	

	/**
	 * 取得已经字节化的持久化对象
	 * 
	 * @return
	 */
	public UserDataPersist getUserDataObj() {
		if(null != userDataPersist){
			if (null != itemMap)
				userDataPersist.itemMapToBytes(itemMap);
			if (null != followingMap)
				userDataPersist.followingMapToBytes(followingMap);
			if (null != fashionMap)
				userDataPersist.fashionMapToBytes(fashionMap);
			if (null != giftList)
				userDataPersist.giftListToBytes(giftList);
			if (null != upgrade)
				userDataPersist.upgradeToBytes(upgrade,pveEpoch,pveNum);
			if (null != matchDeq)
				userDataPersist.matchToBytes(matchDeq);
			return userDataPersist;
		}
		return null;
	}

	public Map<Integer, UserItem> getItemMap() {
		if (null == itemMap) {
			UserDataPersist userData = getUserDataPersist();
			itemMap = userData.initItemMap();
		}
		return itemMap;
	}

	public Map<Long, UserFollow> getFollowingMap() {
		if (null == followingMap) {
			UserDataPersist userData = getUserDataPersist();
			followingMap = userData.initFollowingMap();
		}
		return followingMap;
	}

	public Map<Integer, UserFashion> getFashionMap() {
		if (null == fashionMap) {
			UserDataPersist userData = getUserDataPersist();
			fashionMap = userData.initFashionMap();
		}
		return fashionMap;
	}

	public List<UserGiftVO> getGiftList() {
		if (null == giftList) {
			UserDataPersist userData = getUserDataPersist();
			giftList = userData.initGiftList();
		}
		return giftList;
	}
	public Deque<UserMatch> getmatchDeq() {
		if (null == matchDeq) {
			UserDataPersist userData = getUserDataPersist();
			matchDeq = userData.initMatchRec();
		}
		return matchDeq;
	}
	

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}


	public BaseConnection getConn() {
		return conn;
	}

	public void setConn(BaseConnection conn) {
		this.conn = conn;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserPersist getUserPersist() {
		return userPersist;
	}

	public void setUserPersist(UserPersist user) {
		this.userPersist = user;
	}

	public void setGiftList(List<UserGiftVO> giftList) {
		this.giftList = giftList;
	}	
	public int[] getNInitUpgrade() {	
		if (null == upgrade) {
			UserDataPersist userData = getUserDataPersist();
			upgrade = userData.initUpgrade(this);
		}
		return upgrade;
	}
	public void setUpgrade(int[] upgrade) {
		this.upgrade = upgrade;
	}
	public int getPveNum() {
		if(EpochUtil.getEpochDay()!=EpochUtil.getEpochDay(pveEpoch)){
			pveNum = 0;
		}
		return 3-pveNum;
	}
	public void addMatch(UserMatch um) {
		Deque<UserMatch> deq = getmatchDeq();
		if(deq.size()>50)
			deq.removeFirst();
		deq.add(um);
		updateData = true;
	}

	public synchronized UserDataPersist getUserDataPersist() {
		if (userDataPersist == null) {
			UserDataService service = SpringService.getBean(UserDataService.class);
			userDataPersist = service.get(id);
			if (userDataPersist == null) {
				userDataPersist = new UserDataPersist(id);
				service.save(id,userDataPersist);
			}
		}
		return userDataPersist;
	}
	public void setUserWealthPersist(UserWealthPersist user) {
		this.userWealthPersist = user;
	}
	public synchronized UserWealthPersist getUserWealthPersist() {
		if (userWealthPersist == null) {
			UserWealthService service = SpringService.getBean(UserWealthService.class);
			userWealthPersist = service.get(id);
			if (userWealthPersist == null) {
				userWealthPersist = new UserWealthPersist(id);
				service.save(id,userWealthPersist);
			}
		}
		return userWealthPersist;
	}
	
	public synchronized UserRankPersist getUserRankPersistByDay() {
		Calendar c = Calendar.getInstance();
		int day = EpochUtil.getEpochDay();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int week = c.get(Calendar.WEEK_OF_YEAR);
		UserRankService service = SpringService.getBean(UserRankService.class);
		//当前无数据
		if (userRankPersist == null) {
			//表中无数据
			userRankPersist = service.getCurrentDayRank(id, day);
			if (userRankPersist == null) {
				userRankPersist = new UserRankPersist(id);
				//userRankPersist.setPrizeid(RankService.PRIZE_ID);
				userRankPersist.setDatenum(day);
				userRankPersist.setWeeknum(week);
				userRankPersist.setMonthnum(month);
				userRankPersist.setYearnum(year);
				service.save(userRankPersist);
			}
		//更新本周
		}else{
			if (userRankPersist.getDatenum()!=day) {
				service.update(userRankPersist);
				
				userRankPersist = new UserRankPersist(id);
				//userRankPersist.setPrizeid(RankService.PRIZE_ID);
				userRankPersist.setDatenum(day);
				userRankPersist.setWeeknum(week);
				userRankPersist.setMonthnum(month);
				userRankPersist.setYearnum(year);
				service.save(userRankPersist);
			}
		}

		return userRankPersist;
	}

	/**
	 * 按周统计可以产生较少资料
	 * @return
	 */
	public synchronized UserRankPersist getUserRankPersistByWeek() {
		Calendar c = Calendar.getInstance();
		int day = EpochUtil.getEpochDay();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int week = c.get(Calendar.WEEK_OF_YEAR);
		UserRankService service = SpringService.getBean(UserRankService.class);
		//当前无数据
		if (userRankPersist == null) {
			//表中无数据
			userRankPersist = service.getCurrentWeekRank(id,RankService.PRIZE_ID,week,year);
			if (userRankPersist == null) {
				userRankPersist = new UserRankPersist(id);
				//userRankPersist.setPrizeid(RankService.PRIZE_ID);
				userRankPersist.setDatenum(day);
				userRankPersist.setWeeknum(week);
				userRankPersist.setMonthnum(month);
				userRankPersist.setYearnum(year);
				service.save(userRankPersist);
			}
		//更新本周
		}else{
			if (//userRankPersist.getPrizeid()!=RankService.PRIZE_ID ||
					userRankPersist.getYearnum()!=year
					|| userRankPersist.getWeeknum()!=week
					|| userRankPersist.getMonthnum()!=month) {
				service.update(userRankPersist);
				
				userRankPersist = new UserRankPersist(id);
				//userRankPersist.setPrizeid(RankService.PRIZE_ID);
				userRankPersist.setDatenum(day);
				userRankPersist.setWeeknum(week);
				userRankPersist.setMonthnum(month);
				userRankPersist.setYearnum(year);
				service.save(userRankPersist);
			}
		}

		return userRankPersist;
	}

	public UserRankPersist getUserRankPersist() {
		return userRankPersist;
	}
	public void setUserRankPersist(UserRankPersist userRankPersist) {
		this.userRankPersist = userRankPersist;
	}

	public void addTotalTime() {
		updateUser = true;
		long online = System.currentTimeMillis() - userPersist.getLoginDate();
		//log.debug("add total online time:{}, uid:{}",online,id);
		userPersist.setTotaltime(userPersist.getTotaltime() + online);
	}
}
/* auto generated code */
/* end generated */
