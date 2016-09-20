package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("匹配中用")
@EventAnno(desc = "", name = "event.pk.matching")
public class PkMatchingVO {
	@DescAnno("房主id")
	private long id;
	@DescAnno("房间name")
	private String name;

	@DescAnno("对战类型（两房间对战还是混战）")
	private int pkType;
	@DescAnno("状态>0表示进行中")
	private int state;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPkType() {
		return pkType;
	}

	public void setPkType(int pkType) {
		this.pkType = pkType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}



}
