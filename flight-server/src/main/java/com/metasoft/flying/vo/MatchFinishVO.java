package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋局结束")
@EventAnno(desc = "", name = "event.match.finish")
public class MatchFinishVO {
	@DescAnno("比赛结束")
	private ChessFinishVO finish;

	public ChessFinishVO getFinish() {
		return finish;
	}

	public void setFinish(ChessFinishVO finish) {
		this.finish = finish;
	}

}
