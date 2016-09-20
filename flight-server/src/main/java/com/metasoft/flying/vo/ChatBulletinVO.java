package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("公告消息")
@EventAnno(desc = "", name = "event.chat.bulletin")
public class ChatBulletinVO {
	@DescAnno("玩家id")
	private long id;
	@DescAnno("性别1男2女")
	private int gender;
	@DescAnno("公告类型")
	private int type;
	@DescAnno("1是0否开了麦克风")
	private int phone;
	@DescAnno("名字")
	private String name;
	@DescAnno("公告")
	private String msg;
	public ChatBulletinVO() {

	}

	public ChatBulletinVO(String msg) {
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

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}
	
}
