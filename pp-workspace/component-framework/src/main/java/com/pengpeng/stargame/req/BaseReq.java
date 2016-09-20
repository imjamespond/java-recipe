package com.pengpeng.stargame.req;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class BaseReq implements Serializable {

	/**
	 * 指令编号
	 */
	protected String cmd;
	private String token;
	
	public BaseReq(){
		
	}
	public BaseReq(String cmd){
		this.cmd = cmd;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean validate() {
		return true;
	}

    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

}
