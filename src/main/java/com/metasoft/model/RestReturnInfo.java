package com.metasoft.model;


public class RestReturnInfo {

	private String success; // true false
	private String errMsg; // SUCCESS为FALSE时的出错信息
	private Object data; // 返回的获取的数据,根据接口不同,返回的数据不同

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public boolean isSuccess() {
		if (this.getSuccess().equals("true")) {
			return true;
		} else {
			return false;
		}
	}

}
