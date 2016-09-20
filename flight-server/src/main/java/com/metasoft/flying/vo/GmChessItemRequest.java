package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

public class GmChessItemRequest extends GeneralRequest {

	@DescAnno("itemId")
	private int itemId;
	@DescAnno("coord")
	private int coord;


	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getCoord() {
		return coord;
	}
	public void setCoord(int coord) {
		this.coord = coord;
	}


}
