package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("pve结束")
@EventAnno(desc = "", name = "event.pve.finish")
public class PveFinishVO {
	@DescAnno("胜利pos")
	private int pos;
	@DescAnno("经验值")
	private int exp;
	@DescAnno("剩下次数")
	private int rest;
	@DescAnno("道具数")
	private int upgrade;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getRest() {
		return rest;
	}

	public void setRest(int rest) {
		this.rest = rest;
	}

	public int getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}

}
