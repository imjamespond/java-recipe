package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.Uid;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-8上午11:17
 */
public class RechargeLog extends BaseEntity<String> {
    private String id;
    private String pid;
    private int amount;
    private Date date;
    private String po;
    private String name;
    private int uid;
    private int all;

    public RechargeLog() {
    }

    public RechargeLog(String pid, int amount,String po,int uid,String name) {
        id = po;
        this.pid = pid;
        this.amount = amount;
        this.po = po;
        date = new Date();
        this.uid=uid;
        this.name=name;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }
}
