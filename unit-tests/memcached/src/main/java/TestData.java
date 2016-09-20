package com.james;
import java.util.Date;

import com.james.memcached.BaseEntity;


public class TestData extends BaseEntity<String>{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6444219326042114794L;
	
	private String pid;
    private boolean enable;
    private Date loginDate;//登陆时间

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return pid;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return pid;
	}

	@Override
	public void setId(String id) {
		pid =id;
		
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	

}
