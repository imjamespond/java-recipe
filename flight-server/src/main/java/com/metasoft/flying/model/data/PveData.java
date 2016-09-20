package com.metasoft.flying.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.flying.net.annotation.DescAnno;

@Table("/data/PVE12.xls")
public class PveData{
	@DescAnno("id")
	protected int id;
	@DescAnno("nembers")
	protected String nembers;
	@DescAnno("plane")
	protected int plane;
	@DescAnno("level")
	protected int level;
	@DescAnno("degree")
	protected int degree;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getNembers() {
		return nembers;
	}
	public void setNembers(String nembers) {
		this.nembers = nembers;
	}
	public int getPlane() {
		return plane;
	}
	public void setPlane(int plane) {
		this.plane = plane;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}

	
}
