package com.metasoft.flying.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.controller.EnrollController;
import com.metasoft.flying.model.FlightDequeTimer;
import com.metasoft.flying.model.FlightMatch;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.data.MatchData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.model.job.MatchLoggerJob;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.common.JobService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.ScheduleService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.vo.ChessRoomVO;
import com.metasoft.flying.vo.HallVO;
import com.metasoft.flying.vo.MatchPlayerVO;
import com.metasoft.flying.vo.general.GeneralResponse;

import edu.emory.mathcs.backport.java.util.Collections;

@Service
public class MatchService {
	private static final Logger logger =  LoggerFactory.getLogger(MatchService.class);

	@Autowired
	private EnrollController enrollController;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private UserService userService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private GameRoomService roomService;
	@Autowired
	private RankService rankService;
	@Autowired
	private JobService jobService;
	@Autowired
	private NpcService npcService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private ConnectionService connectionService;
	// private Set<RoomVO> roomOfRandomSet;
	@Value("${match.begin.timer}")
	private String matchBeginStr;
	@Value("${match.enroll.timer}")
	private String matchEnrollStr;
	@Value("${room.check.period}")
	private int roomCheckPeriod;
	@Autowired
	private LocalizationService localizationService;

	private static int kLastMatchID = 11;
	private static int kClearMatch = 11;
	private MatchData curMatch;
	private Set<Long> enroll = new HashSet<Long>();
	private Set<Long> enrollConfine = new HashSet<Long>();

	private Map<Integer, Deque<FlightDequeTimer>> matchDeqMap = new HashMap<Integer, Deque<FlightDequeTimer>>();//比赛id:棋局全部队列
	public Map<Integer, List<MatchPlayerVO>> matchRankMap = new HashMap<Integer, List<MatchPlayerVO>>();//比赛id:排行
	public Map<Integer, Map<Long,Integer>> matchMyRankMap = new HashMap<Integer, Map<Long,Integer>>();//比赛id:排行位置
	public Map<Long, MatchPlayerVO> matchResult = new HashMap<Long, MatchPlayerVO>();//uid,比赛结果

	private MatchBeginTimer matchBeginTimer = new MatchBeginTimer();
	private MatchEnrollTimer maTimer = new MatchEnrollTimer();
	// @PostConstruct
	public void init() {
		scheduleService.scheduleAtFixedRate(new MatchTimer(), DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND * 10);
		/*String[] matches = matchBeginStr.split(";");	
		for (String match : matches) {
			scheduleService.schedule(matchBeginTimer, match);
		}*/
		scheduleService.schedule(matchBeginTimer, matchBeginStr);
		scheduleService.schedule(maTimer, matchEnrollStr);
		maTimer.run();
	}
	
	public void test(){
		matchBeginTimer.commence( new FlightDequeTimer() {
			@Override
			public void checkDeque() {
				// match end
				if (this.getDequeSize() == 0) {
					
				}
			}
		});
	}

	public void enroll(long uid) throws GeneralException {
		synchronized (enroll) {
			if (enroll.contains(uid)) {
				throw new GeneralException(0, "already.enroll");
			}
			if (enrollConfine.contains(uid)) {//TODO
				throw new GeneralException(0, "match.limit");
			}
			enroll.add(uid);
			enrollConfine.add(uid);
		}
	}

	public int enrollCheck(long uid) {
		if (enroll.contains(uid)) {
			return 1;
		}
		return 0;
	}
	
	public boolean enrollConfine(long uid){
		return enrollConfine.contains(uid);
	}

	public void remove(long uid) throws GeneralException {
		synchronized (enroll) {
			enroll.remove(uid);
		}
	}

	
	public MatchData getCurMatch() {
		return curMatch;
	}
	public int getMatchSize() {
		return matchDeqMap.size();
	}
	
	public void awardMatch(int mid) {
		try {
			Set<MatchPlayerVO> set = new TreeSet<MatchPlayerVO>(new Comparator<MatchPlayerVO>(){
				@Override
				public int compare(MatchPlayerVO o1, MatchPlayerVO o2) {
					//由大到小
					if(o1.getScore()<o2.getScore()){
						return 1;
					}else if(o1.getScore() == o2.getScore()){
						if(o1.getElapsed()>o2.getElapsed()){
							return 1;
						}
					}
					return -1;
				}
			});
			
			for(Entry<Long, MatchPlayerVO> entry : matchResult.entrySet()){
				set.add(entry.getValue());
			}
			matchResult.clear();
			
			int rank = 0;
			int mscore = 0;
			List<MatchPlayerVO> listVO = new ArrayList<MatchPlayerVO>();
			Map<Long, Integer> map = new HashMap<Long, Integer>();
			for (MatchPlayerVO vo : set) {
				rank++;
				if (rank == 1) {
					mscore = 10;
				} /*else if (rank == 2) {
					mscore = 5;
				} else if (rank == 3) {
					mscore = 5;
				} else if (rank > 3 && rank <= 30) {
					mscore = 5;
				}*/
				//奖励积分
				if (mscore > 0) {
					User user = userService.getAnyUserById(vo.getUserId());
					user.addCredit(mscore, localService.getLocalString("match.rank", new String[] { String.valueOf(rank) }));
					MatchData md = staticDataService.matchMap.get(mid);
					if (rank == 1&&null!=md) {
						String msg = localService.getLocalString("match.award", new String[] {user.getUserPersist().getNickname(), String.valueOf(md.getTime()), md.getName(), String.valueOf(rank) });
						HallVO hvo = new HallVO(msg);
						connectionService.broadcast(GeneralResponse.newObject(hvo));
					}
				}
				
				listVO.add(vo);
				map.put(vo.getUserId(), rank);

				// 记录名次
				jobService.produce(new MatchLoggerJob(vo.getUserId(), 0, rank, System.currentTimeMillis()));

				// 清零
				mscore = 0;
			}

			if(listVO.size()>0){
				matchRankMap.put(mid, listVO);
				matchMyRankMap.put(mid, map);
			}
			
			// 提供web榜单缓存
			//jobService.produce(new MatchRankJob(listVO));
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 棋局定时器
	 * @author james
	 *
	 */
	private class MatchTimer implements Runnable {

		@Override
		public void run() {
			Iterator<Entry<Integer, Deque<FlightDequeTimer>>> it = matchDeqMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, Deque<FlightDequeTimer>> entry = it.next();
				int mid = entry.getKey();
				Deque<FlightDequeTimer> deqs = entry.getValue();
				//队列非当前比赛,或最后一场结束
				if((curMatch!=null&&mid!=curMatch.getId())||(curMatch==null&&mid==kLastMatchID)){
					if(deqs.size()==0){//所有场次均完成
						awardMatch(mid);
						it.remove();
					}
				}
				
				Iterator<FlightDequeTimer> deqsIt = deqs.iterator();
				while(deqsIt.hasNext()){
					FlightDequeTimer deq = deqsIt.next();
					deq.run();
					if(deq.getDequeSize()==0){//所有棋局均结束
						deqsIt.remove();
					}
				}
			}
		}
	}

	/**
	 * 比赛开始报名定时器
	 * @author james
	 *
	 */
	private class MatchEnrollTimer implements Runnable {
		/**
		 * 配置中指定比赛开始报名时间点,第一次清除
		 */
		public void matchEnroll() {
			Calendar ca = Calendar.getInstance();
			//23点结束报名
			if(curMatch!=null&&curMatch.getId()==kLastMatchID){
				curMatch=null;
			}
			//matchId++;
			int hour = ca.get(Calendar.HOUR_OF_DAY);
			for(Entry<Integer, MatchData> entry:staticDataService.matchMap.entrySet()){
				MatchData md = entry.getValue();
				if(hour==md.getTime()){//时间匹配
					logger.debug("{},{}",md.getName(),md.getId());
					curMatch = md;
					Deque<FlightDequeTimer> deqs = new ArrayDeque<FlightDequeTimer>();
					matchDeqMap.put(curMatch.getId(), deqs);
				}
			}
			//11点清除昨天成绩
			if(curMatch==null&&hour==kClearMatch){
				matchRankMap.clear();
				matchMyRankMap.clear();
			}
			
			//每次开始清除报名记录
			enrollConfine.clear();//TODO
		}

		@Override
		public void run() {
			/*
			 * logger.debug("match score"); if(mark==1){ // 增加比赛兑换积分 //mscore();
			 * rankService.matchLastRank(); //消除当日有比赛 mark = 0; }
			 */
			matchEnroll();
		}
	}
	
	/**
	 * 比赛开始定时器
	 * @author james
	 *
	 */
	private class MatchBeginTimer implements Runnable {

		public void commence(FlightDequeTimer deq) {
			List<Long> list = new ArrayList<Long>();
			synchronized (enroll) {
				// 清除
				list.addAll(enroll);
				enroll.clear();
			}
			
			Collections.shuffle(list);
			npcService.resetMatchNpcPlayer();
			if(list.size()<8){
				commenceLessThan8(list, deq);
			}else {
				commenceLager(list, deq);
			}

		}
		
		private void commenceLager(List<Long> list, FlightDequeTimer deq) {
			int count = 0;
			FlightMatch match = null;
			GameRoom room = null;
			ChessRoomVO vo = null;			
			for (Long uid : list) {
				// filter offline users
				//User user = userService.getOnlineUserById(uid);
				User user = null;
				try {user = userService.getAnyUserById(uid);} catch (GeneralException e) {}//FIXME
				if (null == user) {
					continue;
				}

				count%=4;//logger.debug("{}",count);
				if (count == 0) {
					String roomName = String.valueOf(uid);
					room = roomService.getGroup(roomName);		
					match = new FlightMatch(room);
					match.reset();// 重置
					match.degree = 5;// 难度
					room.clean();// 清理房间,然后设置房间..
					room.setFlightMatch(match);
					// 广播房间资讯
					vo = VoFactory.getChessRoomVO(room);
					vo.setNickname(user.getUserPersist().getNickname());
					vo.setAvatar(user.getUserPersist().getAvatar());
				}
				if (null != match && null != room && null != vo) {
					// match.addGameRoom(room);
					int pos = match.join(uid);
					if (pos >= 0) {
						match.putMatchPlayer(uid, pos);
					}
					join(room, user);

					BaseConnection conn = user.getConn();
					if (null != conn) {
						conn.deliver(GeneralResponse.newObject(vo));
					}
					if (count == 3) {
						match.begin();
						deq.addFlightDeq(match);
					}
					count++;
				}
			}	
			
			// 不足4人的房间补足4人
			if (null != match && match.getPlayerNum() < 4) {
				for (int i = 0; i < 4; i++) {
					if (match.chessPlayers[i] == null) {
						Long npc = npcService.getNextPlayer();
						match.join(npc, i);
						match.setNpc(i);
						match.putMatchPlayer(npc, i);
					}
				}
				match.begin();
				deq.addFlightDeq(match);
			}
		}

		private void commenceLessThan8(List<Long> list,FlightDequeTimer deq) {
			FlightMatch match = null;
			GameRoom room = null;
			ChessRoomVO vo = null;
			for (Long uid : list) {
				// filter offline users
				User user = userService.getOnlineUserById(uid);
				if (null == user) {
					continue;
				}

				//if (count == 0) {
					String roomName = String.valueOf(uid);
					room = roomService.getGroup(roomName);		
					match = new FlightMatch(room);
					match.reset();// 重置
					match.degree = 5;// 难度
					room.clean();// 清理房间,然后设置房间..
					room.setFlightMatch(match);
					// 广播房间资讯
					vo = VoFactory.getChessRoomVO(room);
					vo.setNickname(user.getUserPersist().getNickname());
					vo.setAvatar(user.getUserPersist().getAvatar());
				//}
				if (null != match && null != room && null != vo) {
					// match.addGameRoom(room);
					int pos = match.join(uid);
					if (pos >= 0) {
						match.putMatchPlayer(uid, pos);
					}
					join(room, user);

					BaseConnection conn = user.getConn();
					if (null != conn) {
						conn.deliver(GeneralResponse.newObject(vo));
					}
			
					// 不足4人的房间补足4人
					if (null != match && match.getPlayerNum() < 4) {
						for (int i = 0; i < 4; i++) {
							if (match.chessPlayers[i] == null) {
								Long npc = npcService.getNextPlayer();
								match.join(npc, i);
								match.setNpc(i);
								match.putMatchPlayer(npc, i);
							}
						}
						match.begin();
						deq.addFlightDeq(match);
					}
				}
			}	
		}

		private void join(GameRoom room, User user) {	
			String group = user.getGroup();
			if (null!=group && !group.equals(room.getName())){
				GameRoom currentRoom = roomService.getGroup(group);
				if(!room.getName().equals(group)) {//不在房主房间
					//移除玩家
					currentRoom.removeUser(user);
				}
				// 离开棋局
				currentRoom.clean();
			}

			// 加入组
			room.addUser(user);
		}
		@Override
		public void run() {
			if(null==curMatch){
				return;
			}
			Deque<FlightDequeTimer> deqs = matchDeqMap.get(curMatch.getId());
			if(null==deqs){
				return;
			}
			
			//单次开始之队列
			FlightDequeTimer fdeq = new FlightDequeTimer() {
				@Override
				public void checkDeque() {
					// match end
					if (this.getDequeSize() == 0) {
						
					}
				}
			};
			commence(fdeq);
			deqs.add(fdeq); 
		}
	}
}