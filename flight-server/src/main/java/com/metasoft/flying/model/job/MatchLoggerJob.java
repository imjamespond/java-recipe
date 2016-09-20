package com.metasoft.flying.model.job;

import com.metasoft.flying.service.common.IJob;
import com.metasoft.flying.service.common.SpringService;
import com.qianxun.model.LoggerMatchPersist;
import com.qianxun.service.LoggerMatchPersistService;

public class MatchLoggerJob extends IJob{
	public LoggerMatchPersist logger;
	public MatchLoggerJob(Long uid, Integer mid, Integer rank, Long time) {

		logger = new LoggerMatchPersist(uid, mid, rank, time);
	}

	@Override
	public void doJob() {
		LoggerMatchPersistService lmService = SpringService.getBean(LoggerMatchPersistService.class);
		lmService.save(logger);
	}

}
