package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("比赛列表")
public class MatchVO  {
	@DescAnno("id")
	private int id;
	@DescAnno("报名金币")
	private int cost;
	@DescAnno("1结束0没结束")
	private int end;
	@DescAnno("比赛名称")
	private String name;
	@DescAnno("比赛时间")
	private int time;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}	
	
}
