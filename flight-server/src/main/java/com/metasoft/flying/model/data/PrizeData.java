package com.metasoft.flying.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.flying.net.annotation.DescAnno;

@Table("/data/prize.xls")
public class PrizeData{
	@DescAnno("活动id")
	protected int id;
	@DescAnno("活动名称")
	protected String name;
	@DescAnno("描述")
	protected String description;
	@DescAnno("开始年")
	protected int start_year;
	@DescAnno("开始月")
	protected int start_month;
	@DescAnno("开始日")
	protected int start_day;
	@DescAnno("开始时")
	protected int start_hour;
	@DescAnno("结束年")
	protected int end_year;
	@DescAnno("结束月")
	protected int end_month;
	@DescAnno("结束日")
	protected int end_day;
	@DescAnno("结束时")
	protected int end_hour;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStart_year() {
		return start_year;
	}
	public void setStart_year(int start_year) {
		this.start_year = start_year;
	}
	public int getStart_month() {
		return start_month;
	}
	public void setStart_month(int start_month) {
		this.start_month = start_month;
	}
	public int getStart_day() {
		return start_day;
	}
	public void setStart_day(int start_day) {
		this.start_day = start_day;
	}
	public int getStart_hour() {
		return start_hour;
	}
	public void setStart_hour(int start_hour) {
		this.start_hour = start_hour;
	}
	public int getEnd_year() {
		return end_year;
	}
	public void setEnd_year(int end_year) {
		this.end_year = end_year;
	}
	public int getEnd_month() {
		return end_month;
	}
	public void setEnd_month(int end_month) {
		this.end_month = end_month;
	}
	public int getEnd_day() {
		return end_day;
	}
	public void setEnd_day(int end_day) {
		this.end_day = end_day;
	}
	public int getEnd_hour() {
		return end_hour;
	}
	public void setEnd_hour(int end_hour) {
		this.end_hour = end_hour;
	}
	
}
