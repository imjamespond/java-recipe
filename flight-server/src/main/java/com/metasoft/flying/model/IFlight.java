package com.metasoft.flying.model;

import com.metasoft.flying.vo.general.GeneralResponse;

public abstract class IFlight {
	
	protected long token = 0;//check时间

	abstract public void broadcast(GeneralResponse gr);

	abstract public boolean check();
	abstract public void end();
	abstract public void finishNotify(int pos);
	public long getToken() {
		return token;
	}

	public void setToken(long token) {
		this.token = token;
	}
}
