package com.pengpeng.stargame.vo.role;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-8上午10:27
 */
public class ChargeReq {
    public int uid;
    public String po;
    public int amount;

    private Date starTime;
    private Date endTime;
    //类型  1查询达人币   2查询VIP
    private int type;

    public ChargeReq() {
    }

    public ChargeReq(int uid, String po, int amount) {
        this.uid = uid;
        this.po = po;
        this.amount = amount;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getStarTime() {
        return starTime;
    }

    public void setStarTime(Date starTime) {
        this.starTime = starTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
