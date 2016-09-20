package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("竞技场结束")
@EventAnno(desc = "", name = "event.pk.end")
public class PkArenaEndVO {
	@DescAnno("竞技场数据")
	private PkArenaInfoVO info;
	@DescAnno("分数列表")
	protected List<PkScoreVO> scoreList;
	public PkArenaInfoVO getInfo() {
		return info;
	}

	public void setInfo(PkArenaInfoVO info) {
		this.info = info;
	}

	public List<PkScoreVO> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<PkScoreVO> scoreList) {
		this.scoreList = scoreList;
	}
	
}
