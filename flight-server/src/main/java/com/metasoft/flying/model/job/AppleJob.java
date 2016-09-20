package com.metasoft.flying.model.job;

import com.metasoft.flying.service.common.IJob;
import com.metasoft.flying.service.common.SpringService;
import com.qianxun.model.ApplePersist;
import com.qianxun.service.ApplePersistService;


public class AppleJob extends IJob {

	private ApplePersist ap;
	
	public AppleJob(Long fromuid, Long touid, Integer number, String nickname){
		ap = new ApplePersist();
		ap.setFromuid(fromuid);
		ap.setTouid(touid);
		ap.setNumber(number);
		ap.setNickname(nickname);
		ap.setTime(System.currentTimeMillis());
	}
	
	@Override
	public void doJob() {
		ApplePersistService as = SpringService.getBean(ApplePersistService.class);
		as.save(ap);
	}

}
