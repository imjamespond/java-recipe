package com.metasoft.flying.model.job;
import com.metasoft.flying.model.User;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.common.IJob;
import com.metasoft.flying.vo.SignInAwardVO;
import com.metasoft.flying.vo.general.GeneralResponse;


public class SignInAwardJob extends IJob {
	private User user;
	
	public SignInAwardJob(User u){
		user = u;
	}
	
	@Override
	public void doJob() {
		BaseConnection conn = user.getConn();
		user.addGold(1000);
		if(conn!=null){
			conn.deliver(GeneralResponse.newObject(new SignInAwardVO(1000)));
		}
	}

}
