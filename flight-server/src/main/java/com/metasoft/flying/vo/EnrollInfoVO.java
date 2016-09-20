package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("报名消息")
public class EnrollInfoVO {
	@DescAnno("1.当前服务器的时间")
	private long time;
	@DescAnno("2.下一局比赛报名开始时间,0为可报名,>0为报名倒计时,依据服务器计算每隔5分开始")
	private long next;
	@DescAnno("3.0没报名1已经报名")
	private int enroll;
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
	public int getEnroll() {
		return enroll;
	}
	public void setEnroll(int enroll) {
		this.enroll = enroll;
	}

	
	
}
