package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("托管广播")
@EventAnno(desc = "", name = "event.auto")
public class ChessAutoVO {
	@DescAnno("0取消托管1托管")
	protected int auto;
	@DescAnno("位置")
	protected int pos;

	public ChessAutoVO() {
	}

	public ChessAutoVO(int auto, int pos) {
		this.auto = auto;
		this.pos = pos;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

}
