package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("私聊消息")
@EventAnno(desc = "", name = "event.talk")
public class TalkVO {
	@DescAnno("玩家id")
	private long id;
	@DescAnno("玩家昵称")
	private String name;
	@DescAnno("聊天消息")
	private String msg;

	public TalkVO() {

	}

	public TalkVO(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
