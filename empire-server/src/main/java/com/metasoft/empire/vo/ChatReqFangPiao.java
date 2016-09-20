package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("放漂消息")
@EventAnno(desc = "", name = "event.chat.fangpiao")
public class ChatReqFangPiao extends ChatRequest {
	private int roleid;
	private String key;
	@DescAnno("数量")
	private int num;
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
