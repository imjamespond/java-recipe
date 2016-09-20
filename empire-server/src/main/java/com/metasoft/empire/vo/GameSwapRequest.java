package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;

@DescAnno("游戏交换消息")
public class GameSwapRequest {
	private int type;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
