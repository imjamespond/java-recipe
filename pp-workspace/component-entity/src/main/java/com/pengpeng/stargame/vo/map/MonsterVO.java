package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;

/**
 * 怪物数据
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-9下午4:44
 */
@Desc("怪物信息")
public class MonsterVO {
    @Desc("怪物id")
    private String id;//怪物id
    @Desc("怪物名称")
    private String name;//怪物名称
    @Desc("怪物等级")
    private int level;//怪物等级
    @Desc("怪物类型:0被动,1主动")
    private int type;
    @Desc("怪物坐标x")
    private int x;//坐标x
    @Desc("怪物坐标y")
    private int y;//坐标y

    @Desc("身像id")
    private String monImage;
    @Desc("头像id")
    private String headImage;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getMonImage() {
        return monImage;
    }

    public void setMonImage(String monImage) {
        this.monImage = monImage;
    }
}
