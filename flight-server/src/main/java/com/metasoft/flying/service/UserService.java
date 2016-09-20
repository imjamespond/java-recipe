package com.metasoft.flying.service;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserDataPersist;
import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.UserRankPersist;
import com.metasoft.flying.model.constant.ErrorCodes;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.model.job.SignInAwardJob;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.common.ScheduleService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.util.EpochUtil;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.general.GeneralResponse;
import com.qianxun.service.ExchangeToFlyingService;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public static int debug = 0;

	private ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<Long, User>();
	private ConcurrentMap<Long, User> userOfflineMap = new ConcurrentHashMap<Long, User>();

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private ExchangeToFlyingService exchangeService;

	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private GameRoomService roomService;

	@Autowired
	private UserPersistService userPersistService;
	@Autowired
	private UserRankService userRankService;
	@Autowired
	private UserDataService userDataService;

	@Value("${user.persist.period}")
	private int userPersistPeriod;

	@Value("${save.on.heartbeat}")
	private int saveOnHeartbeat;

	private int heartbeatCount = 0;
	public long launchpoint = System.currentTimeMillis();

	@PostConstruct
	public void init() {
		if (debug == 0) {
			Runnable runnable = new PesistenceTask();
			scheduleService.scheduleAtFixedRate(runnable, DateUtils.MILLIS_PER_MINUTE, DateUtils.MILLIS_PER_MINUTE
					* userPersistPeriod);
			exchangeService.subscribe();
		}
	}

	@PreDestroy
	public void destroy() {
		onPersist();
	}

	public User getRequestUser() throws GeneralException {
		BaseConnection conn = RequestUtils.getCurrentConn();

		if (null == conn) {
			throw new GeneralException(ErrorCodes.UNKOWN_CONNECTION, "UNKOWN_CONNECTION");
		}
		if (0 == conn.getUserId()) {
			throw new GeneralException(ErrorCodes.UNKNOWN_USER, "INVALID_USER");
		}
		User user = userMap.get(conn.getUserId());
		if (null == user)
			throw new GeneralException(ErrorCodes.UNKNOWN_USER, "UNKNOWN_USER");
		return user;
	}

	public User getAnyUserById(long userId) throws GeneralException {
		User user = getOnlineUserById(userId);
		if (user == null) {
			user = userOfflineMap.get(userId);
			if (user == null) {
				UserPersist userPersist = userPersistService.get(userId);
				if (userPersist == null) {
					throw new GeneralException(0, "invalid.userId");
				}
				user = new User();
				user.setId(userId);
				user.setUserPersist(userPersist);
				userOfflineMap.put(userId, user);
			}
		}
		return user;
	}

	public User getOnlineUserById(long userId) {
		User user = this.userMap.get(userId);
		if (null == user) {
			return null;
		}
		return user;
	}

	public User getUserBySessionId(Integer sessionId) {
		BaseConnection conn = connectionService.getConnBySessionId(sessionId);

		if (null == conn)
			return null;
		if (0 == conn.getUserId())
			return null;
		return userMap.get(conn.getUserId());
	}

	public int getOnlineCount() {
		return this.userMap.size();
	}

	public int getOfflineCount() {
		return this.userOfflineMap.size();
	}

	public boolean containUser(long userId) {
		return this.userMap.containsKey(Long.valueOf(userId));
	}

	public User onSignIn(User user) throws GeneralException {
		// current connection user
		BaseConnection conn = RequestUtils.getCurrentConn();
		if(null==conn){
			throw new GeneralException(0, "UNKOWN_CONNECTION");
		}
		User currentUser = getOnlineUserById(conn.getUserId());
		if (currentUser != null) {
			onSignout(currentUser, currentUser.getConn().getSessionId());
		}

		// multi sign in
		User duplicateUser = getOnlineUserById(user.getId());
		User returnUser = null;
		if (duplicateUser != null) {
			final BaseConnection preConn = duplicateUser.getConn();
			if (preConn != null) {
				if (preConn.getSessionId() != conn.getSessionId()) {
					logger.info("multiple signin uid:{}, session:{}", preConn.getUserId(), preConn.getSessionId());
					//update conn immediately
					duplicateUser.setConn(conn);
					duplicateUser.addTotalTime();
					//beware of removing disconnected user in map
					preConn.setUserId(0l);
					preConn.sendAndClose(GeneralResponse.newError("ERROR_MULTI_SIGNIN", ErrorCodes.ERROR_MULTI_SIGNIN));
				}
			}
			returnUser = duplicateUser;
		} else {
			returnUser = user;
			//FIXME in case of this conn is closed asynchronized by netty
			synchronized(conn){
				if(conn.authorized())
					userMap.put(user.getId(), user);
			}
		}

		// offline user
		userOfflineMap.remove(returnUser.getId());

		// clean some data
		cleanData(returnUser);
		
		//登录统计
		UserPersist up = returnUser.getUserPersist();
		int lastDate = EpochUtil.getEpochDay(up.getLoginDate());
		int today = EpochUtil.getEpochDay();
		if(lastDate != today){
			//连续登录
			if(lastDate+1==today){
				up.setConsecutive(up.getConsecutive() + 1);
			}else{
				up.setConsecutive(0);
			}
			up.setTotaldays(up.getTotaldays()+1);
			//第一次登陆奖励
			RequestUtils.putJob(new SignInAwardJob(returnUser));
		}
		up.setLoginDate(System.currentTimeMillis());
		//user与连接关联
		returnUser.setConn(conn);
		//连接与id关联
		conn.setUserId(returnUser.getId());
		returnUser.updateUser = true;
		return returnUser;
	}

	public void onSignout(Integer sessionId) {
		User user = getUserBySessionId(sessionId);
		onSignout(user, sessionId);
	}

	public void onSignout(User user,Integer sessionId) {
		if (null != user) {
			logger.debug("user signout id:{}", user.getId());
			user.addTotalTime();
			
			if (null != user.getGroup()) {
				GameRoom room = roomService.getGroupEntity(user.getGroup());
				Flight flight = room.getFlight();
				if(null != flight){
					flight.offline(user.getId());
					room.removeUser(user);					
				}
			}

			if(sessionId != null && user.getConn()!=null){
				if (sessionId == user.getConn().getSessionId()) {
					user.setConn(null);
					//user.setGroup(null);//断线重进房需要
					userMap.remove(user.getId());
					userOfflineMap.put(user.getId(), user);
				}else{
					logger.warn("session not match current:{}, session:{}", sessionId, user.getConn().getSessionId());
				}
			}else{
				logger.warn("invalid session or connection, uid: {}", user.getId());
			}
		}
	}
	
	public void removeUser(long uid) {
		User user = userMap.remove(uid);	
		if(null != user){
			userOfflineMap.put(uid, user);
			user.setConn(null);
		}
	}

	public void onPersist() {
		if (heartbeatCount % saveOnHeartbeat == 0) {
			onPersistOffline();
		}
		onPersistOnline();
	}

	public void onPersistOffline() {
		for (Entry<Long, User> entry : userOfflineMap.entrySet()) {
			User user = entry.getValue();
			persistUser(user);
		}
		userOfflineMap.clear();
	}

	public void onPersistOnline() {
		for (Entry<Long, User> entry : userMap.entrySet()) {
			User user = entry.getValue();
			persistUser(user);
		}
	}
	
	private void persistUser(User user){
		UserRankPersist ur = user.getUserRankPersist();
		if(null != ur && user.updateRank){
			userRankService.update(ur);
			user.updateRank = false;
		}
		UserDataPersist ud = user.getUserDataObj();
		if(null != ud && user.updateData){
			userDataService.update(ud);
			user.updateData = false;
		}
		if(user.updateUser){
			userPersistService.update(user.getUserPersist());
			user.updateUser = false;
		}
	}

	private void cleanData(User user) {
		UserPersist up = user.getUserPersist();
		int lastDate = EpochUtil.getEpochDay(up.getLoginDate());
		int today = EpochUtil.getEpochDay();
		//清除赠送列表
		if(lastDate != today){
			//user.setGiftList(new ArrayList<UserGiftVO>(1));
		}
	}

	private class PesistenceTask implements Runnable {

		@Override
		public void run() {
			heartbeatCount++;
			onPersist();
		}

	}

	public Set<Entry<Long, User>> getOnlineUserEntrySet() {
		return userMap.entrySet();
	}
}
