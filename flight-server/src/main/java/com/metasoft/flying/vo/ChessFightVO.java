package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("飞机战斗数据")
public class ChessFightVO {
	@DescAnno("飞越坐标")
	protected int coord;
	@DescAnno("飞越坠落棋子")
	protected List<ChessVO> crash;

	public int getCoord() {
		return coord;
	}

	public void setCoord(int coord) {
		this.coord = coord;
	}

	public List<ChessVO> getCrash() {
		return crash;
	}

	public void setCrash(List<ChessVO> crash) {
		this.crash = crash;
	}

}
