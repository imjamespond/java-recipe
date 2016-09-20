package com.metasoft.flying.model.job;

import com.metasoft.flying.service.common.IJob;
import com.metasoft.flying.service.common.SpringService;
import com.qianxun.model.LoggerPersist;
import com.qianxun.service.LoggerPersistService;


public class LoggerJob extends IJob {
	private LoggerPersist lp;
	
	public LoggerJob(int num, int current, int type, long uid, String discription){
		lp = new LoggerPersist();
		lp.setLogDate(System.currentTimeMillis());
		lp.setNum(num);
		lp.setType(type);
		lp.setUid(uid);
		lp.setCurrent(current);
		lp.setDiscription(discription);
	}
	
	@Override
	public void doJob() {
		LoggerPersistService as = SpringService.getBean(LoggerPersistService.class);
		as.save(lp);
	}

}
