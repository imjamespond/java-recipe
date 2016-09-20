package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("财富通知")
@EventAnno(desc = "", name = "event.wealth")
public class WealthVO {
	@DescAnno("积分")
	private int score;
	@DescAnno("玫瑰")
	private int rose;
	@DescAnno("金币")
	private int gold;
	@DescAnno("魅力")
	private int charm;
	@DescAnno("贡献")
	private int contribute;
	@DescAnno("苹果")
	private int apple;

	public WealthVO() {

	}

	public WealthVO(int score, int rose, int gold, int charm, int contribute, int apple) {
		super();
		this.score = score;
		this.rose = rose;
		this.gold = gold;
		this.charm = charm;
		this.contribute = contribute;
		this.apple = apple;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}



	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getCharm() {
		return charm;
	}

	public void setCharm(int charm) {
		this.charm = charm;
	}

	public int getContribute() {
		return contribute;
	}

	public void setContribute(int contribute) {
		this.contribute = contribute;
	}

	public int getApple() {
		return apple;
	}

	public void setApple(int apple) {
		this.apple = apple;
	}



}
