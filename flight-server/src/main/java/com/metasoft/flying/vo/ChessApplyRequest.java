package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("下棋申请请求")
public class ChessApplyRequest extends GeneralRequest {

	@DescAnno("一朵玫瑰数")
	private int rose1;
	@DescAnno("一束玫瑰数")
	private int rose99;
	@DescAnno(" 1.自己取消，2.房主取消，3.飞行器  4对战 5 第三款游戏")
	private int type;
	@DescAnno("玩家id")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRose1() {
		return rose1;
	}

	public void setRose1(int rose1) {
		this.rose1 = rose1;
	}

	public int getRose99() {
		return rose99;
	}

	public void setRose99(int rose99) {
		this.rose99 = rose99;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
