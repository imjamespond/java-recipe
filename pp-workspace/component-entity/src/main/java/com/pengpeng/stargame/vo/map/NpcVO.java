package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;

/**
 * npc数据
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-9下午4:44
 */
@Desc("npc信息")
public class NpcVO {
    @Desc("id")
    private String id;//npcid
    @Desc("npc名称")
    private String name;//npc名称
    @Desc("npc等级")
    private int level;//npc等级
    @Desc("坐标x")
    private int x;//坐标x
    @Desc("坐标y")
    private int y;//坐标y

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
}
