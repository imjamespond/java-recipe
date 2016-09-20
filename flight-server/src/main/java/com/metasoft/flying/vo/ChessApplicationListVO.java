package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("申请下棋列表")
@EventAnno(desc = "", name = "event.application")
public class ChessApplicationListVO {

	private List<ChessApplicationVO> list;

	private long now;

	public List<ChessApplicationVO> getList() {
		return list;
	}

	public void setList(List<ChessApplicationVO> list) {
		this.list = list;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

}