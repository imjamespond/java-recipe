package com.metasoft.flying.vo.general;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("基本请求")
public class GeneralRequest {
	@DescAnno("请求回调序列")
	private int serial;
	@DescAnno("请求命令")
	private String cmd;
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


	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeneralRequest[action=" + this.cmd);
		builder.append(", serial=" + this.serial+"]");
		return builder.toString();
	}
}