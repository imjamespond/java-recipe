package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther james@pengpeng.com
 * @since: 13-10-15上午10:27
 */
@Desc("游戏币达人币等")
public class CoinReq {
    private int uid;
    private String pid;
    @Desc("1是游戏币,2为达人币")
    private int type;
    private int amount;

    public CoinReq() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
