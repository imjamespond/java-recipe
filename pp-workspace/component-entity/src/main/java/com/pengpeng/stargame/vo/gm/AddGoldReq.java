package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午12:04
 */
@Desc("加达人币操作")
public class AddGoldReq {
    @Desc("要加多少达人币")
    private int gold;
    @Desc("Uid 数组")
    private String[] uids;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String[] getUids() {
        return uids;
    }

    public void setUids(String[] uids) {
        this.uids = uids;
    }
}
