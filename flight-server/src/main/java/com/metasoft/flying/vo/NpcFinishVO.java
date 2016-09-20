package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("npc结束")
@EventAnno(desc = "", name = "event.npc.finish")
public class NpcFinishVO {
	@DescAnno("胜利pos")
	private int pos;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
