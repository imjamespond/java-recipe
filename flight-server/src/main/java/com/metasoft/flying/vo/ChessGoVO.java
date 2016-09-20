package com.metasoft.flying.vo;

import java.util.Deque;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("飞机战斗数据")
public class ChessGoVO {
	@DescAnno("行程-2表示在跑道中")
	protected int journey;
	@DescAnno("步进,1表示向前,-1表示向后")
	protected int step;
	@DescAnno("坠落棋子有效16位")
	protected int crash;
	@DescAnno("一起来的棋子有效16位")
	protected int restart;
	@DescAnno("自己的棋子有效16位")
	protected int chess;
	@DescAnno("动画类型,1为反弹,4为荆棘坠毁,其它为事件的itemid")
	protected int type;
	
	public ChessGoVO(int coord, int step, int crash, int restart, int chess, int type) {
		super();
		this.journey = coord;
		this.step = step;
		this.crash = crash;
		this.restart = restart;
		this.chess = chess;
		this.type = type;
	}

	public ChessGoVO(){
		step = 1;
	}	
 
	public int getJourney() {
		return journey;
	}

	public void setJourney(int journey) {
		this.journey = journey;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getCrash() {
		return crash;
	}

	public void setCrash(int crash) {
		this.crash = crash;
	}

	public int getRestart() {
		return restart;
	}

	public void setRestart(int restart) {
		this.restart = restart;
	}

	public int getChess() {
		return chess;
	}

	public void setChess(int chess) {
		this.chess = chess;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public static String debug(Deque<ChessGoVO> goList) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(ChessGoVO vo : goList){
			sb.append(String.format("ChessGoVO: coord:%d step:%d crash:%d chess:%d restart:%d type:%d\n", vo.getJourney(), vo.getStep(), vo.getCrash(), vo.getChess(), vo.getRestart(), vo.getType()));
		}
		return sb.toString();
	}

}
