package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("pk伤害请求")
public class PkHurtRequest extends GeneralRequest {
	@DescAnno("对象位置")
	private int pos;
	@DescAnno("目标位置")
	private int target;
	@DescAnno("伤害值")
	private int hurt;
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public int getHurt() {
		return hurt;
	}
	public void setHurt(int hurt) {
		this.hurt = hurt;
	}

}
