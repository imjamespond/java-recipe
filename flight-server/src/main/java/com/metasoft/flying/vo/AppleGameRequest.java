package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("苹果游戏")
public class AppleGameRequest extends GeneralRequest {
	@DescAnno("苹果数")
	private int apple;

	public int getApple() {
		return apple;
	}

	public void setApple(int apple) {
		this.apple = apple;
	}

}
