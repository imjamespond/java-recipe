package com.pengpeng.stargame.vo.farm.box;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午2:31
 */
@Desc("农场宝箱VO")
public class FarmBoxVO {
    @Desc("0表示没有宝箱  1表示有开锁的 2表示未开锁的")
    private int status;
    @Desc("开锁宝箱需要多少达人币")
    private int gold;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
