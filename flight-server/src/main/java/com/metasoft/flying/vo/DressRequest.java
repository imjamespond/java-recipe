package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("着装请求")
public class DressRequest extends GeneralRequest {

	@DescAnno("着装,自定如0为发型1为脸型2为上装3为下装")
	private int pos;
	@DescAnno("服装id")
	private int itemId;
	@DescAnno("id")
	private long userId;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}