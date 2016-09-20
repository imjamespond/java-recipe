package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋局随机事件")
@EventAnno(desc = "", name = "event.incident")
public class ChessIncidentVO {
	@DescAnno("坐标")
	protected int coord;
	@DescAnno("随机事件 0飞到终点 1飞机坠毁 ")
	protected int type;
	@DescAnno("随机事件说明")
	protected String incident;
	@DescAnno("棋子们")
	protected List<ChessVO> chesses;

	public ChessIncidentVO() {
		super();
	}

	public ChessIncidentVO(int coord, int type) {
		super();
		this.coord = coord;
		this.type = type;
	}

	public int getCoord() {
		return coord;
	}

	public void setCoord(int coord) {
		this.coord = coord;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIncident() {
		return incident;
	}

	public void setIncident(String incident) {
		this.incident = incident;
	}

	public List<ChessVO> getChesses() {
		return chesses;
	}

	public void setChesses(List<ChessVO> chesses) {
		this.chesses = chesses;
	}

}
