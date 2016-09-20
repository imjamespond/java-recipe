package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("聊天消息")
@EventAnno(desc = "", name = "event.chat")
public class ChatRequest {
	private long uid;
	@DescAnno("聊天id")
	private int id;
	@DescAnno("1为聊天,2为邀请,4为放漂")
	private int type;
	private String msg;
	private String name;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
