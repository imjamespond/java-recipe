package com.metasoft.empire.vo;

import com.metasoft.empire.common.GeneralRequest;
import com.metasoft.empire.common.annotation.DescAnno;

public class RankRequest extends GeneralRequest {
	@DescAnno("偏移")
	private int offset;
	@DescAnno("一页数量")
	private int size;
	@DescAnno("id")
	private int id;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
