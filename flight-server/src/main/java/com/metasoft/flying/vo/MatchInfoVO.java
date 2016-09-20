package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("比赛信息")
@EventAnno(desc = "", name = "event.match.info")
public class MatchInfoVO {
	@DescAnno("剩余时间")
	private long remain;
	@DescAnno("棋子信息")
	private ChessInfoVO info;
	public long getRemain() {
		return remain;
	}
	public void setRemain(long remain) {
		this.remain = remain;
	}
	public ChessInfoVO getInfo() {
		return info;
	}
	public void setInfo(ChessInfoVO info) {
		this.info = info;
	}



}
