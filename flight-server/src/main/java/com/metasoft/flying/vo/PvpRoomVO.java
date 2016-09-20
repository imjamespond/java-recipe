package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("pvp匹配事件")
@EventAnno(desc = "", name = "event.pvp.room")
public class PvpRoomVO {

	@DescAnno("房间id")
	private String name;
	@DescAnno("房主名称")
	private String nickname;
	@DescAnno("房主头像")
	private String avatar;
	@DescAnno("状态>0表示进行中")
	private int state;
	@DescAnno("房间类型0为飞行棋1为对战2为比赛")
	protected int type;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
