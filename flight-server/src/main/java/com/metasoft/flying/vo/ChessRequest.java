package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("棋子请求")
public class ChessRequest extends GeneralRequest {
	@DescAnno("玩家id")
	private long userId;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
