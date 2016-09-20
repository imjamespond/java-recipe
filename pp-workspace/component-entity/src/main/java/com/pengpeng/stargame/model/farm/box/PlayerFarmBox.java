package com.pengpeng.stargame.model.farm.box;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午12:28
 */
public class PlayerFarmBox extends BaseEntity<String> {
    private String pid;
    /**
     * 刷新时间
     */
    private Date refreshDate;
    /**
     * 宝箱状态 1未上锁  2 上锁
     */
    private int boxstatu;
    /**
     * 今日打开的 未上锁的宝箱的数量
     */
    private int openboxnum;
    /**
     * 今日打开的 所有宝箱的数量
     */
    private int allboxnum;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }

    public int getBoxstatu() {
        return boxstatu;
    }

    public void setBoxstatu(int boxstatu) {
        this.boxstatu = boxstatu;
    }

    public int getOpenboxnum() {
        return openboxnum;
    }

    public void setOpenboxnum(int openboxnum) {
        this.openboxnum = openboxnum;
    }

    public int getAllboxnum() {
        return allboxnum;
    }

    public void setAllboxnum(int allboxnum) {
        this.allboxnum = allboxnum;
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
