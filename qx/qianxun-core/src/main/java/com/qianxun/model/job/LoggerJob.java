package com.qianxun.model.job;

import com.qianxun.model.LoggerPersist;
import com.qianxun.service.LoggerPersistService;
import com.qianxun.service.SpringService;


public class LoggerJob extends IJob {
	public static final int TYPE_ROSE = 1;// 玫瑰
	public static final int TYPE_GEM = 2;// 钻石
	public static final int TYPE_MATCH = 5;//比赛兑换积分
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
