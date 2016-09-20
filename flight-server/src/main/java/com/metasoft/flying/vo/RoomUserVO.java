package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("聊天室成员")
@EventAnno(desc = "", name = "event.roomuser")
public class RoomUserVO {
	@DescAnno("Id")
	private long userId;
	@DescAnno("昵称")
	private String userName;
	@DescAnno("0离开1加入2踢走")
	private int state;
	@DescAnno("么么值")
	private int contribute;
	@DescAnno("经验分数")
	private int score;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getContribute() {
		return contribute;
	}

	public void setContribute(int contribute) {
		this.contribute = contribute;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
