package com.metasoft.flying.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.FlightRp;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserDataPersist;
import com.metasoft.flying.model.UserRankPersist;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.common.JobService;
import com.metasoft.flying.service.common.ScheduleService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.util.EpochUtil;
import com.metasoft.flying.vo.MatchPlayerVO;
import com.metasoft.flying.vo.PlayerGoldVO;
import com.metasoft.flying.vo.PlayerPveVO;
import com.metasoft.flying.vo.PlayerScoreVO;
import com.metasoft.flying.vo.RankUserVO;
import com.metasoft.flying.vo.RoomVO;

@Service
public class RankService implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(RankService.class);
	
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private GameRoomService chessRoomService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserWealthService userWealthService;
	@Autowired
	private UserDataService userDataService;
	@Autowired
	private UserRankService userRankPsService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private GameRoomService roomService;
	@Autowired
	private JobService jobService;
	
	@Value("${rank.period}")
	private String rankPeriod;
	@Value("${rp.rank.period}")
	private String rpRankPeriod;
	
	//private Runnable rankTask;
	//private Runnable rpRankTask;
	private int yestoday = 0;

	private static final int SIZE = 1000;
	public static int PRIZE_ID = 0;
	public static long PRIZE_START = 0;
	public static long PRIZE_END = 0;

	public List<RankUserVO> girlList;
	public Map<Long, Integer> girlMap;// userId->rank

	public List<RankUserVO> girlWeekList;
	public Map<Long, Integer> girlWeekMap;// userId->rank
	
	public List<RankUserVO> lastWeekList;
	public Map<Long, Integer> lastWeekMap;// userId->rank

	public List<RankUserVO> contributeList;
	public Map<Long, Integer> contributeMap;// userId->rank
	
	public List<RankUserVO> rankPrizeList;
	public Map<Long, Integer> rankPrizeMap;// userId->rank
	
	public List<MatchPlayerVO> matchDayRank;//日排行
	public Map<Long, Integer> matchDayRankMap;
	public Map<Long, Integer> matchDayScoreMap;
	
	public List<MatchPlayerVO> matchLastDayRank;//昨日排行
	public Map<Long, Integer> matchLastDayRankMap;
	
	public List<MatchPlayerVO> matchWeekRank;//昨日排行
	public Map<Long, Integer> matchWeekRankMap;
	
	public List<PlayerScoreVO> expRank;//比赛总排行
	public Map<Long, Integer> expRankMap;
	
	public List<PlayerPveVO> pveRank;//pve排行
	public Map<Long, Integer> pveRankMap;
	
	public List<PlayerGoldVO> goldRank;//gold排行
	public Map<Long, Integer> goldRankMap;
	
	public RoomVO[] roomList = new RoomVO[GeneralConstant.ROOM_LIST_SIZE];
	public int roomWaiting = 0;
	public int roomAvailable = 0;
	public long fingerprint = 0;
	
	//@PostConstruct
	public void init() {
		logger.debug("RankService initializing...");
		
		experienceRank();
		pveRank();
		int day = EpochUtil.getEpochDay();
		matchRank(day-1);//昨天
		matchLastRank();
		matchRank(day);//今天
		matchWeekRank();

		scheduleService.schedule(new RankTask(), rankPeriod);
		scheduleService.schedule(new RpRankTask(), rpRankPeriod);
	}

	/**
	 * 
	 
	public void rankGirlList() {

		List<UserPersist> list = userPsService.getFemaleByRoseDesc(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<RankUserVO> listVO = new ArrayList<RankUserVO>(SIZE);
		for (UserPersist up : list) {
			RankUserVO vo = new RankUserVO();
			vo.setUserId(up.getId());
			vo.setUserName(up.getNickname());
			vo.setRose(up.getCharm());
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}

		girlList = listVO;
		girlMap = map;
	}

	public void rankGirlLastWeekList() {
		int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1;
		List<UserRankPersist> list = userRankPsService.getFemaleByRoseDesc(0, SIZE, week);
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<RankUserVO> listVO = new ArrayList<RankUserVO>(SIZE);
		//有分数
		for (UserRankPersist up : list) {		
			RankUserVO vo = new RankUserVO();		
			vo.setUserId(up.getFuid());
			vo.setUserName(up.getUsername());		
			if(null!=up.getSumnum())
				vo.setRose(up.getSumnum());			
			listVO.add(vo);
			map.put(up.getFuid(), rank++);
		}

		lastWeekList = listVO;
		lastWeekMap = map;
	}


	public void rankGirlWeekList() {
		int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		List<UserRankPersist> list = userRankPsService.getFemaleByRoseDesc(0, SIZE, week);
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<RankUserVO> listVO = new ArrayList<RankUserVO>(SIZE);
		//有分数
		for (UserRankPersist up : list) {		
			RankUserVO vo = new RankUserVO();		
			vo.setUserId(up.getFuid());
			vo.setUserName(up.getUsername());		
			if(null!=up.getSumnum())
				vo.setRose(up.getSumnum());			
			listVO.add(vo);
			map.put(up.getFuid(), rank++);
		}

		girlWeekList = listVO;
		girlWeekMap = map;
	}*/


	/**
	 * 
	 
	public void rankMoreContribute() {

		List<UserPersist> list = userPsService.getByContributeDesc(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<RankUserVO> listVO = new ArrayList<RankUserVO>(SIZE);
		for (UserPersist up : list) {
			RankUserVO vo = new RankUserVO();
			vo.setUserId(up.getId());
			vo.setUserName(up.getNickname());
			vo.setContribute(up.getContribute());
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}

		contributeList = listVO;
		contributeMap = map;
	}*/

	
	/**
	 * 活动排行
	
	public void rankPrizeList(int prizeId) {
		List<UserRankPersist> list = null;
		if(prizeId > 0)
			list = userRankPsService.getByApplePrizeDesc(0, SIZE, prizeId);
		else
			list = new ArrayList<UserRankPersist>();
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<RankUserVO> listVO = new ArrayList<RankUserVO>(10);
		for (UserRankPersist up : list) {
			RankUserVO vo = new RankUserVO();
			vo.setUserId(up.getUid());
			
			try {
				User user = userService.getAnyUserById(up.getUid());
				vo.setUserName(user.getUserPersist().getNickname());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			
			
			vo.setRose(up.getSumnum());
			listVO.add(vo);
			map.put(up.getUid(), rank++);
		}

		rankPrizeList = listVO;
		rankPrizeMap = map;
	} */
	
	/**
	 * 活动刷新

	public void updatePrize() {
		Map<Integer, PrizeData> map = staticDataService.getPrizeDataMap();
		for (Entry<Integer, PrizeData> entry : map.entrySet()) {
			PrizeData pd = entry.getValue();
			long now = System.currentTimeMillis();
			Calendar start = Calendar.getInstance();
			start.set(pd.getStart_year(), pd.getStart_month()-1, pd.getStart_day(), pd.getStart_hour(), 0);
			Calendar end = Calendar.getInstance();
			end.set(pd.getEnd_year(), pd.getEnd_month()-1, pd.getEnd_day(), pd.getEnd_hour(), 0);
			RankService.PRIZE_START = start.getTimeInMillis();
			RankService.PRIZE_END = end.getTimeInMillis();			
			//开始的
			if(now>=start.getTimeInMillis()&&now<end.getTimeInMillis()){
				RankService.PRIZE_ID = pd.getId();
				//logger.debug(pd.getName());
				break;
			}
			//还未开始
			else if(now<start.getTimeInMillis()){
				break;
			}
			//结束的?
			else{
				RankService.PRIZE_ID = 0;
			}
		}
	}
	 */	
	/**
	 * 比赛排行
	 */
	public void matchRank(int ep){
		//排序实现
		/*
		Set<MatchPlayerVO> set = new TreeSet<MatchPlayerVO>(new Comparator<MatchPlayerVO>(){
			@Override
			public int compare(MatchPlayerVO o1, MatchPlayerVO o2) {

				//o2前,o1后
				if(o1.getScore()<o2.getScore()){
					return 1;
				}
				//
				else if(o1.getScore() == o2.getScore()){
					if(o1.getElapsed()==0){
						return 1;
					}
					if(o2.getElapsed()==0){
						return -1;
					}
					//由小到大
					if(o1.getElapsed()>o2.getElapsed()){
						return 1;
					}else if(o1.getElapsed()==o2.getElapsed()){
						if(o1.getRank()>o2.getRank()){
							return 1;
						}
					}
				}
				return -1;
			}
		});*/
		//查询当日得分合计
		List<UserRankPersist> list = userRankPsService.getByMatchByDayDesc(0, SIZE, ep);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		Map<Long, Integer> map2 = new HashMap<Long, Integer>();
		int rank = 1;
		List<MatchPlayerVO> listVO = new ArrayList<MatchPlayerVO>(SIZE);
		for (UserRankPersist up : list) {
			//MatchPlayer mp = matchService.getMatchPlayer(up.getUid());		
			MatchPlayerVO vo = new MatchPlayerVO();
			vo.setUserId(up.getUid());
			vo.setScore(up.getMatch());		
			map2.put(vo.getUserId(), up.getMatch());
//			if(null != mp){//Acquire match elapsed;
//				vo.setRank(mp.rank);
//				vo.setElapsed(mp.elapsed);
//			}
			vo.setUserName(up.getUsername());	
			vo.setElapsed(up.getElapsed());
			
			//set.add(vo);
			map.put(vo.getUserId(), rank++);
			listVO.add(vo);
		}
		/*
		int rank = 1;
		List<MatchPlayerVO> listVO = new ArrayList<MatchPlayerVO>(SIZE);
		for (MatchPlayerVO vo : set) {
			map.put(vo.getUserId(), rank++);
			listVO.add(vo);
		}*/	

		matchDayRank = listVO;
		matchDayRankMap = map;
		matchDayScoreMap= map2;
	}	
	
	public void matchLastRank(){
		//int day = EpochUtil.getEpochDay(System.currentTimeMillis()-GeneralConstant.SINGLE_DAY);
		//List<UserRankPersist> list = userRankPsService.getByMatchByDayDesc(0, 10, day);

		matchLastDayRank = new ArrayList<MatchPlayerVO>(matchDayRank);
		matchLastDayRankMap = new HashMap<Long,Integer>(matchDayRankMap);
		//matchDayRank.clear();
		//matchDayRankMap.clear();
	}
	/**
	 * 比赛周排行
	 */
	public void matchWeekRank() {
		int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		List<UserRankPersist> list = userRankPsService.getByMatchByWeekDesc(0, SIZE, week);
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<MatchPlayerVO> listVO = new ArrayList<MatchPlayerVO>(SIZE);
		for (UserRankPersist up : list) {
			MatchPlayerVO vo = new MatchPlayerVO();
			try {
				User user = userService.getAnyUserById(up.getUid());
				vo.setUserName(user.getUserPersist().getNickname());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			vo.setUserId(up.getUid());
			vo.setScore(up.getSumnum());		
			vo.setElapsed(up.getElapsed());
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}

		matchWeekRank = listVO;
		matchWeekRankMap = map;
	}
	/**
	 * 经验值排行
	 */
	public void experienceRank() {

		List<UserDataPersist> list = userDataService.getByScoreDesc(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();

		int rank = 1;
		List<PlayerScoreVO> listVO = new ArrayList<PlayerScoreVO>(SIZE);
		for (UserDataPersist up : list) {
			PlayerScoreVO vo = new PlayerScoreVO();
			vo.setUserId(up.getId());
			try {
				User user = userService.getAnyUserById(up.getId());
				vo.setUserName(user.getUserPersist().getNickname());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			vo.setScore(up.getScore());
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}

		expRank = listVO;
		expRankMap = map;
	}
	
	public void pveRank(){
		List<UserDataPersist> list = userDataService.getByPveRank(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		
		int rank = 1;
		List<PlayerPveVO> listVO = new ArrayList<PlayerPveVO>(SIZE);
		for (UserDataPersist up : list){
			PlayerPveVO vo = new PlayerPveVO();
			vo.setUserId(up.getId());
			try {
				User user = userService.getAnyUserById(up.getId());
				vo.setUserName(user.getUserPersist().getNickname());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			vo.setPve(up.getPve());
			vo.setPvetime(up.getPvetime());
			
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}
		
		pveRank = listVO;
		pveRankMap = map;
	}
	
	public void goldRank(){
		List<UserDataPersist> list = userDataService.getByGoldRank(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		
		int rank = 1;
		List<PlayerGoldVO> listVO = new ArrayList<PlayerGoldVO>(SIZE);
		for (UserDataPersist up : list){
			PlayerGoldVO vo = new PlayerGoldVO();
			vo.setUserId(up.getId());
			try {
				User user = userService.getAnyUserById(up.getId());
				vo.setUserName(user.getUserPersist().getNickname());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			vo.setGold(up.getGold());
			
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}
		
		goldRank = listVO;
		goldRankMap = map;
	}
	
	/**
	 * 排名任务
	 * @author james
	 *
	 */
	private class RankTask implements Runnable {

		@Override
		public void run() {
			//logger.debug("RankService");
			
			//updatePrize();
			
			//rankGirlList();
			//rankMoreContribute();
			//rankMoreContributeWeekly();
			//rankGirlWeekList();
			//rankGirlLastWeekList();

			//rankPrizeList(RankService.PRIZE_ID);

			experienceRank();
			pveRank();
			
			int epoch = EpochUtil.getEpochDay();
			if(yestoday!=epoch){
				goldRank();
				//matchLastRank();
				matchWeekRank(); 
				yestoday = epoch;
			}
			//matchRank(epoch);
		}
	}
	
	private class RpRankTask implements Runnable {

		@Override
		public void run() {
			//Rp王
			try {
				FlightRp.awardRpRank();
			} catch (GeneralException e) {
				e.printStackTrace();
			}
		}
	}	

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
