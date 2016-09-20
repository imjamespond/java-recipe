package com.metasoft.flying.model;

import com.metasoft.flying.model.exception.GeneralException;

public class UserItem {
	private int itemId;
	private int itemNum;
	private long itemTime;

	public UserItem(int itemId, int itemNum) {
		super();
		this.itemId = itemId;
		this.itemNum = itemNum;
	}

	public UserItem(int itemId, int itemNum, long itemTime) {
		super();
		this.itemId = itemId;
		this.itemNum = itemNum;
		this.itemTime = itemTime;
	}

	public void increase(int num) {
		itemNum += num;
	}

	public void decrease(int num) throws GeneralException {
		if (itemNum < num) {
			throw new GeneralException(0, "insuffitcient.item." + itemId);
		}
		itemNum -= num;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public long getItemTime() {
		return itemTime;
	}

	public void setItemTime(long itemTime) {
		this.itemTime = itemTime;
	}
}
