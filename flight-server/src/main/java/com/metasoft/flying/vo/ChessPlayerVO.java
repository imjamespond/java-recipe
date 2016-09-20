package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋子信息")
@EventAnno(desc = "", name = "event.chessinfo")
public class ChessPlayerVO {
	@DescAnno("玩家id")
	protected long userId;
	@DescAnno("回合")
	protected int round;
	@DescAnno("棋子所属0,1,2,3号玩家")
	protected int pos;
	@DescAnno("所有棋子 ChessVO[]")
	private ChessVO[] chesses;
	@DescAnno("所有道具数量 位置:0魔力色子1动力装置2空中加油3魔力控制色子4礼盒 short[]")
	private int[] items;
	@DescAnno("昵称")
	private String name;
	@DescAnno("赠送规定的玫瑰")
	private int rose;
	@DescAnno("魔力色子使用数")
	private int magicDice;
	@DescAnno("展示飞机次数")
	private int show;
	@DescAnno("自动状态")
	private int autoState;
	@DescAnno("飞机")
	protected int plane;
	@DescAnno("1为npc0 为否")
	protected int npc;
	
	@DescAnno("空中加油次数")
	protected int refuel = 0;//
	@DescAnno("荆棘buff,以bit表示")
	protected int thorns = 0;//
	@DescAnno("迷雾buff,以bit表示")
	protected int fog = 0;//
	
	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public ChessVO[] getChesses() {
		return chesses;
	}

	public void setChesses(ChessVO[] chesses) {
		this.chesses = chesses;
	}

	public int[] getItems() {
		return items;
	}

	public void setItems(int[] items) {
		this.items = items;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public int getAutoState() {
		return autoState;
	}

	public void setAutoState(int autoState) {
		this.autoState = autoState;
	}

	public int getPlane() {
		return plane;
	}

	public void setPlane(int plane) {
		this.plane = plane;
	}

	public int getRefuel() {
		return refuel;
	}

	public void setRefuel(int refuel) {
		this.refuel = refuel;
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

	public int getNpc() {
		return npc;
	}

	public void setNpc(int npc) {
		this.npc = npc;
	}

}
