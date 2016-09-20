package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋局成员")
@EventAnno(desc = "", name = "event.chessroomuser")
public class ChessRoomUserVO {
	@DescAnno("Id")
	private long userId;
	@DescAnno("昵称")
	private String name;
	@DescAnno("位置")
	private int pos;
	@DescAnno("赠送规定的玫瑰")
	private int rose;
	@DescAnno("魔力色子使用数")
	private int magicDice;
	@DescAnno("展示飞机次数")
	private int show;
	@DescAnno("自动状态")
	private int autoState;

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

	public int getAutoState() {
		return autoState;
	}

	public void setAutoState(int autoState) {
		this.autoState = autoState;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}

	public int getMagicDice() {
		return magicDice;
	}

	public void setMagicDice(int magicDice) {
		this.magicDice = magicDice;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

}
