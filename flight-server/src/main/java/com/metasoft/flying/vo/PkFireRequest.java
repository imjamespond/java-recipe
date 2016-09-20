package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("pk开火请求")
public class PkFireRequest extends GeneralRequest {
	@DescAnno("对象位置")
	private int pos;
	@DescAnno("目标位置")
	private int target;
	@DescAnno("攻击类型1导弹2...")
	private int type;
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
}
