package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("物品更新通知")
@EventAnno(desc = "", name = "event.item")
public class ItemVO {
	@DescAnno("到期")
	private long deadline;
	@DescAnno("道具id")
	private int itemId;
	@DescAnno("数量")
	private int num;

	public ItemVO(long deadline, int itemId, int num) {
		super();
		this.deadline = deadline;
		this.itemId = itemId;
		this.num = num;
	}

	public ItemVO() {
		super();
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
