package com.metasoft.flying.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.flying.net.annotation.DescAnno;

@Table("/data/item.xls")
public class ItemData{
	@DescAnno("道具id")
	protected int id;
	@DescAnno("道具名称")
	protected String name;
	@DescAnno("道具描述")
	protected String description;
	@DescAnno("价格")
	protected int cost;
	@DescAnno("图标")
	protected int icon;
	@DescAnno("时效")
	protected long deadline;
	protected int effect;
	protected int num;
	protected int rose;

	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
	}

	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
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
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getRose() {
		return rose;
	}
	public void setRose(int rose) {
		this.rose = rose;
	}
}
