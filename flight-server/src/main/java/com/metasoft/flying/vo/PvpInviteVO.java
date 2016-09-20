package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("pvp邀请")
@EventAnno(desc = "", name = "event.pvp.invite")
public class PvpInviteVO {
	@DescAnno("玩家id")
	private long id;
	@DescAnno("名字")
	private String name;
	public PvpInviteVO() {

	}

	public PvpInviteVO(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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
}
