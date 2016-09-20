package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午12:07
 */
@Desc("添加达人币VO")
public class AddGoldVO {
    @Desc("Uid 数组")
    private String[] uids;

    @Desc("达人币")
    private int gold;

    public String[] getUids() {
        return uids;
    }

    public void setUids(String[] uids) {
        this.uids = uids;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
