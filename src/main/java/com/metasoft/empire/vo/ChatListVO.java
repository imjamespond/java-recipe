package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("消息列表")
@EventAnno(desc = "", name = "event.chat.list")
public class ChatListVO {
	private ChatRequest[] list;

	public ChatRequest[] getList() {
		return list;
	}

	public void setList(ChatRequest[] list) {
		this.list = list;
	}


}
