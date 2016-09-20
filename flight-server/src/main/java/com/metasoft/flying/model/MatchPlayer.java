package com.metasoft.flying.model;

public class MatchPlayer  {
	public int finish;//完成数量
	public int standby;//待飞
	public int jouney;//最远的
	public int pos;//位置
	public int rank;//名次
	public long elapsed;//结束时间
	private long userid;
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public MatchPlayer(long userid) {
		super();
		this.userid = userid;
	}
	
/*
	public int compareTo(MatchPlayer o) {
		
		if(finish>o.finish){
			return 1;
		}else if(finish == o.finish){
			if(standby>o.standby){
				return 1;
			}else if(standby == o.standby){
				if(jouney>o.jouney){
					return 1;
				}
			}
		}
		return -1;
	}
	*/
}
