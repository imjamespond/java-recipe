package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

public class RoomVO{
	@DescAnno("房主id")
	private long id;
	@DescAnno("昵称")
	private String name;
	@DescAnno("房间总人数")
	private int num;
	@DescAnno("房间状态")
	private int state;
	@DescAnno("房主性别")
	private int gender;
	@DescAnno("申请人数")
	private int apply;
	@DescAnno("1在线,0不在线")
	private boolean online;
	
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getApply() {
		return apply;
	}

	public void setApply(int apply) {
		this.apply = apply;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
