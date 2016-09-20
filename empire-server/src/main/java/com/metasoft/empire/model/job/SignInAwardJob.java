package com.metasoft.empire.model.job;

import com.metasoft.empire.model.User;
import com.metasoft.empire.net.BaseConnection;
import com.metasoft.empire.service.common.IJob;


public class SignInAwardJob extends IJob {
	private User user;
	
	public SignInAwardJob(User u){
		user = u;
	}
	
	@Override
	public void doJob() {
		BaseConnection<?> conn = user.getConn();
		//user.addGold(1000);
		if(conn!=null){
			//conn.deliver(GeneralResponse.newObject(new SignInAwardVO(1000)));
		}
	}

}
