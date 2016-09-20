package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;
@EventAnno(desc = "", name = "event.chess.fog")
@DescAnno("使用迷雾")
public class ChessFogVO {
	@DescAnno("buff16位有效")
	private int buff;

	public int getBuff() {
		return buff;
	}

	public void setBuff(int buff) {
		this.buff = buff;
	}
	
}
