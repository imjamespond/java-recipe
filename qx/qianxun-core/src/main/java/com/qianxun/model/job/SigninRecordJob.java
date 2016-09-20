package com.qianxun.model.job;

import com.qianxun.service.SpringService;
import com.test.qianxun.model.SigninRecord;
import com.test.qianxun.model.User;
import com.test.qianxun.service.SigninRecordService;
import com.test.qianxun.service.UserService;


public class SigninRecordJob extends IJob {
	private SigninRecord sr;
	private User user;
	
	public SigninRecordJob( User user, String ip){
		sr = new SigninRecord();
		sr.setDate_(System.currentTimeMillis());
		sr.setIp(ip);
		sr.setUid(user.getId());
		this.user=user;
	}
	
	@Override
	public void doJob() {
		UserService userService = SpringService.getBean(UserService.class);
		userService.update(user);
		SigninRecordService srs = SpringService.getBean(SigninRecordService.class);
		srs.save(sr);
	}

}
