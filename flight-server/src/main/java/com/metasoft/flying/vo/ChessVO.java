package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("棋子信息")
public class ChessVO {
	@DescAnno("路程")
	protected int journey;//
	@DescAnno("当前坐标")
	protected int curCoord;//
	@DescAnno("玩家位置0-3")
	protected int pos;//
	@DescAnno("棋子位置0,1,2,3号")
	protected int chessPos;//
	@DescAnno("状态0未起飞1起飞中2飞行中")
	protected int state;

	public int getJourney() {
		return journey;
	}

	public void setJourney(int journey) {
		this.journey = journey;
	}

	public int getCurCoord() {
		return curCoord;
	}

	public void setCurCoord(int curCoord) {
		this.curCoord = curCoord;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

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
