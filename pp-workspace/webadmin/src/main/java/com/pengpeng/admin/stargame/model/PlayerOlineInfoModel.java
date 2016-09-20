package com.pengpeng.admin.stargame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午11:22
 */
@Entity
@Table(name="gm_player_oline")
public class PlayerOlineInfoModel {
    @Id
    private String id;
    @Column
    private int maxNum;
    @Column
    private int minNum;
    @Column
    private Date maxTime;
    @Column
    private Date minTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }
}
