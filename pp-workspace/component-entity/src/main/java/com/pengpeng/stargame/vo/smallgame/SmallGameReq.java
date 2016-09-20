package com.pengpeng.stargame.vo.smallgame;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("小游戏请求")
public class SmallGameReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("积分")
    private int score;
    @Desc("游戏")
    private int type;
    @Desc("购买金额")
    private int gold;
    @Desc("数字")
    private int num;//亲妈点击数

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
