package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("大厅消息")
@EventAnno(desc = "", name = "event.hall")
public class HallVO {
	@DescAnno("玩家id")
	private long id;
	@DescAnno("玩家昵称")
	private String name;
	@DescAnno("聊天消息")
	private String msg;

	public HallVO() {

	}

	public HallVO(String msg) {
		this.msg = msg;
	}
	
	public HallVO(String msg,long uid,String uname) {
		this.msg = msg;
		id = uid;
		name = uname;
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
