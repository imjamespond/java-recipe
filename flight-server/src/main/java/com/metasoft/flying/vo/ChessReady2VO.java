package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋子准备信息出发卡用")
@EventAnno(desc = "", name = "event.chessready2")
public class ChessReady2VO {
	@DescAnno("棋子所属0,1,2,3号玩家")
	protected int pos;
	@DescAnno("棋子位置0,1,2,3号")
	protected int chessPos;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getChessPos() {
		return chessPos;
	}

	public void setChessPos(int chessPos) {
		this.chessPos = chessPos;
	}
}
