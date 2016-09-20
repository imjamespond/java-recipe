package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("升级请求")
public class PveUpgradeRequest extends GeneralRequest {
	@DescAnno("按照棋局道具位置分配点数")
	private int[] upgrade;

	public int[] getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(int[] upgrade) {
		this.upgrade = upgrade;
	}

}
