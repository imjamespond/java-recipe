package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;

@DescAnno("兑换")
public class ExchangeReq {
	@DescAnno("score")
	private int score;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
