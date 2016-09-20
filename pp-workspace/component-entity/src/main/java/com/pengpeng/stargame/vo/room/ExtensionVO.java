package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-22
 * Time: 下午5:04
 */
@Desc("扩建信息VO")
public class ExtensionVO {

    @Desc("需要的农场等级")
    private int  level;
    @Desc("需要的游戏币")
    private int gameCoin;
    @Desc("需要的时间 毫秒数")
    private int time;  //
    @Desc("结束时间 毫秒数")
    private long  endTime;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
