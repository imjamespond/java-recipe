package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("色子")
@EventAnno(desc = "", name = "event.dice")
public class ChessDiceVO {
	@DescAnno("位置")
	protected int pos;
	@DescAnno("色子数")
	protected int dice;
	@DescAnno("色子数2")
	protected int dice2;
	@DescAnno("使用道具-1表示没有,0-7表示对应道具")
	protected int item;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos % 4;
	}

	public int getDice() {
		return dice;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}

	public int getDice2() {
		return dice2;
	}

	public void setDice2(int dice2) {
		this.dice2 = dice2;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}


}
