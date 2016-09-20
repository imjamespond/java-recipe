package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("苹果游戏消息")
public class AppleInfoVO {
	@DescAnno("1.当前服务器的时间")
	private long time;
	@DescAnno("2.下一局敲苹果的开始时间")
	private long next;
	@DescAnno("3.这个玩家下一局是否使用了魔力锤子（0表示没有，2表示使用了，而且每敲一次的倍数是2，获取两个苹果）")
	private int hammer;
	@DescAnno("4.购买魔力锤子的钻石数（20钻）")
	private int price;
	@DescAnno("5.每局游戏游戏时间（30秒）")
	private int duration;
	@DescAnno("上次苹果数")
	private int apple;
	@DescAnno("活动id")
	private int prizeid;
	@DescAnno("活动name")
	private String name;
	@DescAnno("活动开始时间")
	private long start;
	@DescAnno("活动结束时间")
	private long end;	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getNext() {
		return next;
	}
	public void setNext(long next) {
		this.next = next;
	}
	public int getHammer() {
		return hammer;
	}
	public void setHammer(int hammer) {
		this.hammer = hammer;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getApple() {
		return apple;
	}
	public void setApple(int apple) {
		this.apple = apple;
	}
	public int getPrizeid() {
		return prizeid;
	}
	public void setPrizeid(int prizeid) {
		this.prizeid = prizeid;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	
	
}
