package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("比分")
public class ChessScoreVO {
	@DescAnno("玩家id")
	protected long userId;
	@DescAnno("玩家昵称")
	protected String nickname;
	@DescAnno("位置")
	protected int pos;
	@DescAnno("分数")
	protected int score;
	@DescAnno("金币")
	protected int gold;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public ChessScoreVO(long userId, int pos, int score) {
		super();
		this.userId = userId;
		this.pos = pos;
		this.score = score;
	}

	public ChessScoreVO() {
		super();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

}
