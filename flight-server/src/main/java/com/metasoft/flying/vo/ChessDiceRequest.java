package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("扔色子请求")
public class ChessDiceRequest extends GeneralRequest {
	@DescAnno("道具位置0-7,参照item表数据")
	private int item;
	@DescAnno("指定玩家位置")
	private int pos;
	@DescAnno("魔力色子数0-6,开局表示设置次数限制")
	private int dice;

	public int getDice() {
		return dice;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}


}
