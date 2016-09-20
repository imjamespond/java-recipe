package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * @auther james
 * @since: 14-5-28下午4:57
 */
public class Bubble extends BaseEntity<String> {
    private String pid;
    //领取次
    private int times;
    //领取时间
    private Date accDate;
    //类型
    private int type;
    //状态
    private int status;

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        this.pid = pid;
    }

    @Override
    public String getKey() {
        return pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
