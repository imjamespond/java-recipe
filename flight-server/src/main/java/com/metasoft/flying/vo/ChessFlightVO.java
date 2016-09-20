package com.metasoft.flying.vo;

import java.util.Deque;
import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋子飞行")
@EventAnno(desc = "", name = "event.flight")
public class ChessFlightVO {
	@DescAnno("座位0,1,2,3")
	private int pos;
	
	@DescAnno("飞行数据 ChessGoVO")
	private Deque<ChessGoVO> goList;

	public Deque<ChessGoVO> getGoList() {
		return goList;
	}

	public void setGoList(Deque<ChessGoVO> goList) {
		this.goList = goList;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
