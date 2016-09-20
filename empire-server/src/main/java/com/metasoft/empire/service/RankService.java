package com.metasoft.empire.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.model.User;
import com.metasoft.empire.model.UserDataPersist;
import com.metasoft.empire.service.common.ScheduleService;
import com.metasoft.empire.vo.RankVO;

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
	private UserService userService;
	@Autowired
	private UserWealthService userWealthService;
	@Autowired
	private UserDataService userDataService;
	
	@Value("${rank.task.period}")
	private String rankPeriod;

	private static final int SIZE = 20;
	public static int PRIZE_ID = 0;
	public static long PRIZE_START = 0;
	public static long PRIZE_END = 0;

	public List<RankVO> rankList;//排行
	public Map<Long, Integer> rankMap;
	public List<RankVO> rankRedeemList;//排行
	public Map<Long, Integer> rankRedeemMap;
	
	@PostConstruct
	public void init() {
		logger.debug("RankService initializing...");
		rank();
		rankRedeem();
		scheduleService.schedule(new RankTask(), rankPeriod);
	}
	
	public void rank(){
		List<UserDataPersist> list = userDataService.getByScoreRank(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		
		int rank = 1;
		List<RankVO> listVO = new ArrayList<RankVO>(SIZE);
		for (UserDataPersist up : list){
			RankVO vo = new RankVO();
			vo.setUid(up.getId());
			try {
				User user = userService.getAnyUserById(up.getId());
				vo.setName(user.getUserPersist().getUsername());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			vo.setScore(up.getScore());
			
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}
		
		rankList = listVO;
		rankMap = map;
	}
	public void rankRedeem(){
		List<UserDataPersist> list = userDataService.getByRedeemDesc(0, SIZE);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		
		int rank = 1;
		List<RankVO> listVO = new ArrayList<RankVO>(SIZE);
		for (UserDataPersist up : list){
			RankVO vo = new RankVO();
			vo.setUid(up.getId());
			try {
				User user = userService.getAnyUserById(up.getId());
				vo.setName(user.getUserPersist().getUsername());
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			vo.setScore(up.getRedeem());
			
			listVO.add(vo);
			map.put(up.getId(), rank++);
		}
		
		rankRedeemList = listVO;
		rankRedeemMap = map;
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

			rank();
			rankRedeem();
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

}
