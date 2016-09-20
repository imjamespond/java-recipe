package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

/**
 * @author james
 * player killing
 */
@DescAnno("攻击消息")
@EventAnno(desc = "", name = "event.pk.fire")
public class PkFireVO {

	@DescAnno("对象位置")
	private int pos;
	@DescAnno("目标位置")
	private int target;
	@DescAnno("攻击类型")
	private int type;
	
	public PkFireVO() {
		super();
	}

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
