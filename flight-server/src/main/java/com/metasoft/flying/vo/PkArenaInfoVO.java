package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("竞技场一般断线后或匹配中用")
public class PkArenaInfoVO {
	@DescAnno("房主id")
	private long id;
	@DescAnno("房间name")
	private String name;
	@DescAnno("对战还剩下多少时间，根据这个时间去显示倒计时。一开始是15分钟")
	private long pkTime;
	@DescAnno("复活倒计时")
	private int rebirth; 
	@DescAnno("道具倒计时（这个先加上吧，暂时也还没用到道具）")
	private int genItem;
	@DescAnno("对战类型（两房间对战还是混战）")
	private int pkType;
	@DescAnno("玩家 List<PkPlayerVO>")
	private List<PkPlayerVO> players;
	@DescAnno("状态0是空闲1是准备2是开始3是流局")
	private int state;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PkPlayerVO> getPlayers() {
		return players;
	}

	public void setPlayers(List<PkPlayerVO> players) {
		this.players = players;
	}



	public long getPkTime() {
		return pkTime;
	}

	public void setPkTime(long pkTime) {
		this.pkTime = pkTime;
	}

	public int getRebirth() {
		return rebirth;
	}

	public void setRebirth(int rebirth) {
		this.rebirth = rebirth;
	}

	public int getGenItem() {
		return genItem;
	}

	public void setGenItem(int genItem) {
		this.genItem = genItem;
	}

	public int getPkType() {
		return pkType;
	}

	public void setPkType(int pkType) {
		this.pkType = pkType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}



}
