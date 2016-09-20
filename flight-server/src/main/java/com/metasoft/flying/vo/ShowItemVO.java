package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("动画事件")
@EventAnno(desc = "", name = "event.animation")
public class ShowItemVO {

	@DescAnno("id")
	private long userId;
	@DescAnno("昵称")
	private String name;
	@DescAnno("动画类型,一般为itemid")
	private int type;
	@DescAnno("剩余次数")
	private int showNum;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getShowNum() {
		return showNum;
	}

	public void setShowNum(int showNum) {
		this.showNum = showNum;
	}

}
