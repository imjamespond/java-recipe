package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

public class GmRequest extends GeneralRequest {
	@DescAnno("GM指令")
	private String gmCmd;
	@DescAnno("数量")
	private int num;
	@DescAnno("玩家id")
	private long userId;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGmCmd() {
		return gmCmd;
	}

	public void setGmCmd(String gmCmd) {
		this.gmCmd = gmCmd;
	}

}
