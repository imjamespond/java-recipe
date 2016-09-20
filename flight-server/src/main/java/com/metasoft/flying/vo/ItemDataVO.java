package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

public class ItemDataVO {

	@DescAnno("道具id")
	protected int id;
	@DescAnno("道具名称")
	protected String name;
	@DescAnno("道具描述")
	protected String discription;
	@DescAnno("价格")
	protected int cost;
	@DescAnno("图标")
	protected int icon;
	@DescAnno("作用(1为玫瑰2为钻石4为在背包出现8为送玫瑰16为飞机24为送玫瑰|飞机32为棋局道具)")
	protected int effect;
	@DescAnno("对应作用的数量")
	protected int num;
	@DescAnno("赠送玫瑰")
	protected int rose;
	@DescAnno("时效")
	protected long deadline;

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

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
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
