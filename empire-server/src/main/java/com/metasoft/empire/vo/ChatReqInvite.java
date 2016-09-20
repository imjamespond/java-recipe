package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("邀请消息")
@EventAnno(desc = "", name = "event.chat.invite")
public class ChatReqInvite extends ChatRequest {
	private int invite;

	public int getInvite() {
		return invite;
	}

	public void setInvite(int invite) {
		this.invite = invite;
	}

}
