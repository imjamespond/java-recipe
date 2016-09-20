package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("棋局房间 请求才返回玩家列表 广播只有房间相关配置")
@EventAnno(desc = "", name = "event.chessroom")
public class ChessRoomVO {
	@DescAnno("房主状态0不在线1不在自己房间2在自己房间")
	private int id;
	@DescAnno("预约时间")
	private long appoint;
	@DescAnno("房间id")
	private String name;
	@DescAnno("房主名称")
	private String nickname;
	@DescAnno("房主头像")
	private String avatar;
	@DescAnno("状态>0表示进行中")
	private int state;
	@DescAnno("聊天等级限制")
	private int level;
	@DescAnno("房主聊天等级限制")
	private int talkLevel;
	@DescAnno("玫瑰限制")
	private int rose;
	@DescAnno("魔力色子限制")
	protected int magic;
	@DescAnno("展示飞机限制")
	protected int show;
	@DescAnno("房间类型0为飞行棋1为对战2为比赛")
	protected int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}

	public int getTalkLevel() {
		return talkLevel;
	}

	public void setTalkLevel(int talkLevel) {
		this.talkLevel = talkLevel;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
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

	public long getAppoint() {
		return appoint;
	}

	public void setAppoint(long appoint) {
		this.appoint = appoint;
	}
	
}
