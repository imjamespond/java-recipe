package com.pengpeng.stargame.model.room;

import com.pengpeng.stargame.model.base.Item;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 下午2:48
 */
public class FashionItem extends Item {

	// 0:未穿 1:已穿
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
