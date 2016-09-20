package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

/**
 * @author james
 * player killing
 */
@DescAnno("行走消息")
@EventAnno(desc = "", name = "event.pk.go")
public class PkGoVO {

	@DescAnno("对象位置")
	protected int pos = 0;//
	@DescAnno("坐标")
	protected int x = 0;
	protected int y = 0;
	
	
	public PkGoVO() {
		super();
	}

	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

}
