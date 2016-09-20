package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
@DescAnno("道具位置 ")
public class ChessItemVO {
	@DescAnno("坐标")
	protected int coord;
	@DescAnno("道具 ")
	protected int itemId;
	public int getCoord() {
		return coord;
	}
	public void setCoord(int coord) {
		this.coord = coord;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
}
