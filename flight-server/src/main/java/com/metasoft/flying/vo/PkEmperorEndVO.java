package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("三国杀结束")
@EventAnno(desc = "", name = "event.pk.emperor.end")
public class PkEmperorEndVO {
	@DescAnno("1主公胜2反贼胜4内奸胜")
	private int state;
	@DescAnno("分数列表")
	protected List<PkScoreVO> scoreList;
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<PkScoreVO> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<PkScoreVO> scoreList) {
		this.scoreList = scoreList;
	}
	
}
