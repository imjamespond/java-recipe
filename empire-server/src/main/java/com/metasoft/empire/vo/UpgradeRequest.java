package com.metasoft.empire.vo;

import com.metasoft.empire.common.GeneralRequest;
import com.metasoft.empire.common.annotation.DescAnno;

public class UpgradeRequest extends GeneralRequest {

	@DescAnno("升级")
	private int id;
	private int num;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
