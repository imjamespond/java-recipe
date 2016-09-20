package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;
@EventAnno(desc = "", name = "event.chess.thorns")
@DescAnno("使用荆棘装甲")
public class ChessThornsVO {
	@DescAnno("buff16位有效")
	private int buff;

	public int getBuff() {
		return buff;
	}

	public void setBuff(int buff) {
		this.buff = buff;
	}
	
}
