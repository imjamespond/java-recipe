package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;

@DescAnno("聊天消息")
public class ChatIdReq {
	@DescAnno("聊天id")
	private int id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
