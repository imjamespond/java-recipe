package com.metasoft.flying.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.flying.net.annotation.DescAnno;

@Table("/data/match.xls")
public class MatchData{
	@DescAnno("id")
	protected int id;
	@DescAnno("花费")
	protected int cost;
	@DescAnno("活动名称")
	protected String name;
	@DescAnno("描述")
	protected int time;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
