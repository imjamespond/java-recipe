package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-26
 * Time: 下午3:11
 */
@Desc("农场加速VO")
public class FarmSpeedVO {
    @Desc("类型 1 化肥 2达人币")
    private int type;
    @Desc("花费的达人币")
    private int gold;
    @Desc("作物的名字")
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
