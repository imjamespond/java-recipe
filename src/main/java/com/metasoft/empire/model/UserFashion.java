package com.metasoft.empire.model;

public class UserFashion {
	private int itemId;
	private long deadline;

	public UserFashion(int itemId, long deadline) {
		super();
		this.itemId = itemId;
		this.deadline = deadline;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

}
