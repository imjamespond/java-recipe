package com.metasoft.empire.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.metasoft.empire.common.annotation.DescAnno;

@DescAnno("基本请求")
public class GeneralRequest {
	@DescAnno("请求回调序列")
	private int serial;
	@DescAnno("请求")
	private String cmd;
	@DescAnno("请求参数")
	private Object param;
	public int getSerial() {
		return this.serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getParam() {
		return param;
	}

	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="type")
	public void setParam(Object param) {
		this.param = param;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeneralRequest[action=" + getCmd());
		builder.append(", serial=" + this.serial+"]");
		return builder.toString();
	}
}