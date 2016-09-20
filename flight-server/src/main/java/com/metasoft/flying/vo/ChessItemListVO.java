package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;
@EventAnno(desc = "", name = "event.chess.item")
@DescAnno("道具列表 ")
public class ChessItemListVO {

	@DescAnno("道具列表 ChessItemVO")
	protected ChessItemVO[] itemList;

	public ChessItemVO[] getItemList() {
		return itemList;
	}

	public void setItemList(ChessItemVO[] itemList) {
		this.itemList = itemList;
	}


}
