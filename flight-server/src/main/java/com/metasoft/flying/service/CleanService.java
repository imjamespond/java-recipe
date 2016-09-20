package com.metasoft.flying.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.service.common.ScheduleService;
import com.metasoft.flying.service.net.ConnectionService;

@Service
public class CleanService {
	private static final Logger logger = LoggerFactory.getLogger(CleanService.class);

	public static int debug = 0;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private GameRoomService chessRoomService;

	@Autowired
	private UserPersistService userPersistenceService;
	@Autowired
	private UserRankService userRankPersistenceService;
	@Value("${clean.task.period}")
	private String cleanJobPeriod;

	//@PostConstruct
	public void init() {
		if (debug == 0) {
			Runnable runnable = new CleanTask();
			scheduleService.schedule(runnable, cleanJobPeriod);
		}
	}

	private class CleanTask implements Runnable {

		@Override
		public void run() {
			logger.debug("CleanTask");
			//清理房间
			chessRoomService.resetGroup();
			//清理排行
			userRankPersistenceService.clean();
			//清理连接
			connectionService.clean();
			System.gc();
		}

	}

}
