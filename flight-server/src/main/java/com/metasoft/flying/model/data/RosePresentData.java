package com.metasoft.flying.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.flying.net.annotation.DescAnno;

@Table("/data/rose_present.xls")
public class RosePresentData{
	public static final int EFFECT1 = 1;
	public static final int EFFECT2 = 2;
	public static final int EFFECT3 = 3;
	
	@DescAnno("道具id")
	protected int id;
	@DescAnno("道具名称")
	protected String name;
	@DescAnno("道具描述")
	protected String description;

	protected int effect;
	protected int num;

	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

}
