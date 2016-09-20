package com.metasoft.flying.vo;

import java.util.Deque;
import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋局随机事件")
@EventAnno(desc = "", name = "event.incident")
public class ChessFortuneVO {
	@DescAnno("物品表中的effect为64物品")
	protected int itemId;
	@DescAnno("itemId的对应坐标")
	protected int coord;
	@DescAnno("棋子们")
	protected Deque<ChessVO> chesses;
	@DescAnno("奖品")
	protected ItemVO prize;
	public ChessFortuneVO(int itemId2) {
		itemId = itemId2;
	}
	public ChessFortuneVO() {

	}

	public int getCoord() {
		return coord;
	}
	public void setCoord(int coord) {
		this.coord = coord;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public Deque<ChessVO> getChesses() {
		return chesses;
	}
	public void setChesses(Deque<ChessVO> chesses) {
		this.chesses = chesses;
	}
	public ItemVO getPrize() {
		return prize;
	}
	public void setPrize(ItemVO prize) {
		this.prize = prize;
	}


}
