package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("谁扔色子")
@EventAnno(desc = "", name = "event.cast")
public class ChessCastVO {
	@DescAnno("位置")
	protected int pos;
	@DescAnno("轮流计数")
	protected int turn;
	@DescAnno("色子计数")
	protected int count;
	@DescAnno("回合")
	protected int round;
	@DescAnno("回合类型0为普通1为6奖励的回合2是空中接力的回合")
	protected int type;
	
	@DescAnno("荆棘buff,以bit表示")
	protected int thorns = 0;//
	@DescAnno("迷雾buff,以bit表示")
	protected int fog = 0;//
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getThorns() {
		return thorns;
	}

	public void setThorns(int thorns) {
		this.thorns = thorns;
	}

	public int getFog() {
		return fog;
	}

	public void setFog(int fog) {
		this.fog = fog;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
