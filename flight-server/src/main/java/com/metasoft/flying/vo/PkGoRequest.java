package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("pk行走请求")
public class PkGoRequest extends GeneralRequest {
	@DescAnno("对象位置")
	private int pos;
	@DescAnno("x坐标")
	private int x;
	@DescAnno("y坐标")
	private int y;

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
