package com.metasoft.empire.vo;

import com.metasoft.empire.common.annotation.DescAnno;
import com.metasoft.empire.common.annotation.EventAnno;

@DescAnno("战斗结束消息")
@EventAnno(desc = "", name = "event.game.end")
public class GameEndVO {
	private long uid;
	private int score;
	public GameEndVO() {
		super();
	}
	public GameEndVO(long uid, int score) {
		super();
		this.uid = uid;
		this.score = score;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
