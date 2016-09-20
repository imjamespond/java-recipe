package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

public class FashionVO {
	@DescAnno("道具id")
	private int itemId;
	@DescAnno("剩余时间")
	private long deadline;

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
