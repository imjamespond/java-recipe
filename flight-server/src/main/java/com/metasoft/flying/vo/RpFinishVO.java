package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("rp王结束")
@EventAnno(desc = "", name = "event.rp.finish")
public class RpFinishVO {
	@DescAnno("胜利pos")
	private int pos;
	@DescAnno("rp时间")
	private long duration;
	@DescAnno("30名排行")
	private List<RpRankVO> list;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<RpRankVO> getList() {
		return list;
	}

	public void setList(List<RpRankVO> list) {
		this.list = list;
	}

}
