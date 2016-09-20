package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

/**
 * @author james
 * 
 */
@DescAnno("受伤通知消息")
@EventAnno(desc = "", name = "event.pk.hurt")
public class PkHurtVO {

	@DescAnno("target位置")
	private int pos;
	@DescAnno("hp")
	private int hp;
	@DescAnno("hurt")
	private int hurt;
	@DescAnno("目标位置")
	private int target;
	@DescAnno("分数")
	private int score;	
	public PkHurtVO() {
		super();
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getHurt() {
		return hurt;
	}

	public void setHurt(int hurt) {
		this.hurt = hurt;
	}

}
