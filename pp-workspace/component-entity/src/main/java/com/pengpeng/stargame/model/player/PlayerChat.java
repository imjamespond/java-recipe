package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * User: mql
 * Date: 13-12-2
 * Time: 下午4:07
 */
public class PlayerChat extends BaseEntity<String> {
    private String pid;
    //刷新时间
    private Date nextTime;
    //已经使用了多少次的免费次数
    private int num;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pid;
    }
}
