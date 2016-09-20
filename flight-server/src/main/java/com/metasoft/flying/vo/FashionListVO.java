package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("着装通知")
@EventAnno(desc = "", name = "event.fashion")
public class FashionListVO {
	@DescAnno("玩家id")
	private long userId;
	@DescAnno("着装列表")
	private List<FashionVO> list;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<FashionVO> getList() {
		return list;
	}

	public void setList(List<FashionVO> list) {
		this.list = list;
	}

}