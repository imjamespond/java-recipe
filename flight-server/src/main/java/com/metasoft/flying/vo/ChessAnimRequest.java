package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("棋子动画完成请求")
public class ChessAnimRequest extends GeneralRequest {

	@DescAnno("回合数")
	private int turn;
	
	@DescAnno("扔色子计数")
	private int count;

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


}
