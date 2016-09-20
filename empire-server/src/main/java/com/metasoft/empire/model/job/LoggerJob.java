package com.metasoft.empire.model.job;

import com.metasoft.empire.service.common.IJob;


public class LoggerJob extends IJob {
	//private LoggerPersist lp;
	
	public LoggerJob(int num, int current, int type, long uid, String discription){

	}
	
	@Override
	public void doJob() {
		//LoggerPersistService as = SpringService.getBean(LoggerPersistService.class);
		//as.save(lp);
	}

}
