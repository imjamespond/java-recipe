package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("登陆信息")
public class LoginRequest extends GeneralRequest {

	@DescAnno("id")
	private long id;
	@DescAnno("昵称,网站的校验码")
	private String name;
	@DescAnno("性别1男2女")
	private int gender;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
