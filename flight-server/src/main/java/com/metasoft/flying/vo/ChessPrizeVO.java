package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("棋局奖励")
public class ChessPrizeVO {
	@DescAnno("物品id")
	protected int itemId;
	@DescAnno("物品名称")
	protected String item;

	public ChessPrizeVO() {
		super();
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

}
