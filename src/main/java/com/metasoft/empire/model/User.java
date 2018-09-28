package com.metasoft.empire.model;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.constant.GeneralConstant;
import com.metasoft.empire.model.job.LoggerJob;
import com.metasoft.empire.net.BaseConnection;
import com.metasoft.empire.service.UserDataService;
import com.metasoft.empire.service.UserWealthService;
import com.metasoft.empire.service.common.JobService;
import com.metasoft.empire.service.common.SpringService;

public class User {

	private static final Logger log = LoggerFactory.getLogger(User.class);

	private long id;
	private String group;
	
	public int map;
	public int[] roles = new int[4];
	public long recieve = 0;

	private BaseConnection<?> conn;
	public void deliver(Object obj){
		if(conn!=null&&conn.authorized())
			conn.deliver(obj);
	}

	public boolean updateUser;
	public boolean updateRank;
	public boolean updateData;
	/**
	 * Created by UserService::getAnyPlayerById()
	 * 
	 */
	public User() {
		updateUser = false;
		updateRank = false;
		updateData = false;
	}
	
	private Map<Integer, UserFashion> fashionMap;// 时装类型如1为上装,2为下装...,时装细节
	private Map<Long, UserFollow> followingMap;// 玩家关注人的map
	private Map<Integer, UserItem> itemMap;
	private Map<Integer, UserUpgrade> upgradeMap;

	// persist obj
	private UserPersist userPersist;
	private UserDataPersist userDataPersist;
	private UserWealthPersist userWealthPersist;

	public WeakReference<GameRoom> gameRoom;
	public GameRoom getGameRoom(){
		if(null==gameRoom)
			return null;
		return gameRoom.get();
	}

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
		
		// log queue
		String causeFormat = String.format("减少钻石 :%s, 原:%d, 现:%d, 描述:%s", userPersist.getUsername(), preGems, userWealth.getGems(), cause);
		log.info(causeFormat);
		JobService jobService = SpringService.getBean(JobService.class);
		jobService.produce(new LoggerJob(-needGems, userWealth.getGems(), GeneralConstant.TYPE_GEM, id, cause));
	}

	public int getScore() {
		UserDataPersist userData = getUserDataPersist();
		return userData.getScore();
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
	public synchronized void addRedeem(int val) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		userData.setRedeem(userData.getRedeem() + val);
	}
	/**
	 * 减分
	 * 
	 * @param score
	 */
	public synchronized void decreaseScore(int score) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		if(userData.getScore()>=score)
			userData.setScore(userData.getScore() - score);
		else
			userData.setScore(0);
	}
	public synchronized boolean decrScore(int score) {
		updateData = true;
		UserDataPersist userData = getUserDataPersist();
		if(userData.getScore()>=score){
			userData.setScore(userData.getScore() - score);
			return true;
		}else
			return false;
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
			if (null != upgradeMap)
				userDataPersist.upgradeMapToBytes(upgradeMap);
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
	
	public Map<Integer, UserUpgrade> getUpgradeMap() {
		if(null == upgradeMap){
			UserDataPersist userData = getUserDataPersist();
			upgradeMap = userData.initUpgradeMap();
		}
		return upgradeMap;
	}
	
	public UserUpgrade getUpgrade(int id) {
		Map<Integer, UserUpgrade> map = getUpgradeMap();
		UserUpgrade uu = map.get(id);
		if(null == uu){
			uu = new UserUpgrade(id, 0, 0, 0);
			map.put(id, uu);
		}
		return uu;
	}
	
	public int getLevel(){
		int level = 0;
		Map<Integer, UserUpgrade> map = getUpgradeMap();
		for(Entry<Integer, UserUpgrade> entry: map.entrySet()){
			UserUpgrade uu = entry.getValue();
			uu.updateLevel();
			level += uu.getLevel();
		}
		return level;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}


	public BaseConnection<?> getConn() {
		return conn;
	}

	public void setConn(BaseConnection<?> conn) {
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
	
	public void addTotalTime() {
		updateUser = true;
		long online = System.currentTimeMillis() - userPersist.getLoginDate();
		//log.debug("add total online time:{}, uid:{}",online,id);
		userPersist.setTotaltime(userPersist.getTotaltime() + online);
	}
}
/* auto generated code */
/* end generated */
