package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("战斗超时消息")
@EventAnno(desc = "", name = "event.game.expired")
public class GameExpiredVO {
	private long uid;
	private int hp;
	
	
	public GameExpiredVO() {
		super();
	}

	public GameExpiredVO(long uid, int hp) {
		this.uid = uid;
		this.hp = hp;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	
}
