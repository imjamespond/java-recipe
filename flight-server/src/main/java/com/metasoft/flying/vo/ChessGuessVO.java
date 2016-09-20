package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("竞猜")
@EventAnno(desc = "", name = "event.guess")
public class ChessGuessVO {
	@DescAnno("位置")
	protected int pos;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public ChessGuessVO() {
		super();
	}

	public ChessGuessVO(int pos) {
		super();
		this.pos = pos;
	}

}
