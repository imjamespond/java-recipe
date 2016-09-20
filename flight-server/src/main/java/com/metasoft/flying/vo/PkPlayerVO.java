package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

/**
 * @author james
 */
@DescAnno("pk玩家消息")
public class PkPlayerVO {

	@DescAnno("昵称")
	protected String name;
	@DescAnno("玩家id")
	protected long userId = 0;
	@DescAnno("队伍,1主公，2忠臣，3反贼，4内奸")
	protected int team = 0;
	@DescAnno("位置")
	protected int pos = 0;
	@DescAnno("坐标")
	protected int x = 0;
	protected int y = 0;
	@DescAnno("hitpoint")
	protected int hp;
	@DescAnno("max hp")
	protected int hpMax = 100;
	@DescAnno("攻击力")
	protected int attack = 100;
	@DescAnno("飞机")
	protected int plane;
	@DescAnno("状态0为挂1为正常")
	protected int state;
	@DescAnno("分数")
	protected int score;
	@DescAnno("重生剩下时间")
	protected long rebirth;
	public PkPlayerVO() {
		super();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getPlane() {
		return plane;
	}

	public void setPlane(int plane) {
		this.plane = plane;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getRebirth() {
		return rebirth;
	}

	public void setRebirth(long rebirth) {
		this.rebirth = rebirth;
	}
}
