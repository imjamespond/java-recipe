package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋局结束")
@EventAnno(desc = "", name = "event.finish")
public class ChessFinishVO {
	@DescAnno("玩家id")
	protected long userId;
	@DescAnno("位置 -1表示棋局退出")
	protected int pos;
	@DescAnno("分数列表")
	protected List<ChessScoreVO> scoreList;

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

	public List<ChessScoreVO> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<ChessScoreVO> scoreList) {
		this.scoreList = scoreList;
	}

}
