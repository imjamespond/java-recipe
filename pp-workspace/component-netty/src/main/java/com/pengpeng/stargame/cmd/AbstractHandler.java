package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.IServerServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractHandler {
    protected Log log = LogFactory.getLog(this.getClass());
	protected IServerServer server;


	public AbstractHandler(){
	}
	public void setServer(IServerServer server) {
		this.server = server;
	}

	public boolean validate() {
		return true;
	}
	
	

}
