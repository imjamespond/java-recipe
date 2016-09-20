package com.chitu.chess;

import java.sql.Timestamp;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gecko.broadcast.api.ConnectorCallbackImpl;
import cn.gecko.commons.timer.ScheduleManager;
import cn.gecko.commons.utils.SpringUtils;

import com.chitu.chess.service.ChessPlayerManager;


public class ServerHealth {

	private static Log healthLog = LogFactory.getLog("health.log");

	@Autowired
	private ScheduleManager scheduleManager;

	@PostConstruct
	public void init() {
		Runnable healthTask = new HealthTask();
		scheduleManager.scheduleAtFixedRate(healthTask, DateUtils.MILLIS_PER_MINUTE, DateUtils.MILLIS_PER_MINUTE);
	}

	private class HealthTask implements Runnable {

		public void run() {
			healthLog.info(new Timestamp(System.currentTimeMillis()));
			healthLog.info("\n\n");
			healthLog.info("******** Schedulers Health Info ********");
			ScheduledThreadPoolExecutor[] schedulers = scheduleManager.getSchedulers();
			int totalActiveCount = 0;
			int totalCorePoolSize = 0;
			long totalTaskCount = 0;
			long totalQueueUseingCapacity = 0;
			StringBuilder activeCount = new StringBuilder();
			StringBuilder corePoolSize = new StringBuilder();
			StringBuilder taskCount = new StringBuilder();
			StringBuilder queueUseingCapacity = new StringBuilder();
			for (int i = 0; i < schedulers.length; ++i) {
				ScheduledThreadPoolExecutor scheduler = schedulers[i];
				int ac = scheduler.getActiveCount();
				totalActiveCount += ac;
				int pool = scheduler.getCorePoolSize();
				totalCorePoolSize += pool;
				long tc = scheduler.getTaskCount();
				totalTaskCount += tc;
				long queue = scheduler.getQueue().size();
				totalQueueUseingCapacity += queue;
				activeCount.append(ac + " , ");
				corePoolSize.append(pool + " , ");
				taskCount.append(tc + " , ");
				queueUseingCapacity.append(queue + " , ");
			}
			activeCount.append(totalActiveCount);
			corePoolSize.append(totalCorePoolSize);
			taskCount.append(totalTaskCount);
			queueUseingCapacity.append(totalQueueUseingCapacity);
			//
			healthLog.info("\tschedulerActiveCount:" + activeCount);
			healthLog.info("\tCorePoolSize:" + corePoolSize);
			healthLog.info("\tTaskCount:" + taskCount);
			healthLog.info("\tschedulerQueueUseingCapacity:" + queueUseingCapacity);
			//
			healthLog.info("******** Callback Executor Info ********");
			ThreadPoolExecutor callbackExecutor = SpringUtils.getBeanOfType(ConnectorCallbackImpl.class).getExecutor();
			healthLog.info("\tcallbackActiveCount:" + callbackExecutor.getActiveCount());
			healthLog.info("\tCorePoolSize:" + callbackExecutor.getCorePoolSize());
			healthLog.info("\tTaskCount:" + callbackExecutor.getTaskCount());
			//
			healthLog.info("******** DataSource Info ********");
			BasicDataSource datasource = SpringUtils.getBeanOfType(BasicDataSource.class);
			healthLog.info("\tdataSourceActiveCount:" + datasource.getNumActive());
			healthLog.info("\tdataSourceIdleCount:" + datasource.getNumIdle());
			healthLog.info("\tmaxActiveCount:" + datasource.getMaxActive());
			healthLog.info("\tmaxIdleCount:" + datasource.getMaxIdle());
			healthLog.info("\tmaxWaitMs:" + datasource.getMaxWait());
			//
			healthLog.info("******** PlayerManager Info ********");
			ChessPlayerManager playerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
			healthLog.info("\tonlineCount:" + playerManager.getOnlineCount());
			healthLog.info("\tofflineCount:" + playerManager.getOfflineCount());
		}
	}
}
