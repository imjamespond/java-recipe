package com.pengpeng.stargame.model.gm;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午12:11
 */
public class AddGoldInfo extends BaseEntity<String > {
    private String id;
    private List<String> uIds;
    private List<String> dayIds;
    private Date nextTime;
    private int gold;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public AddGoldInfo(){

    }

    public List<String> getuIds() {
        return uIds;
    }

    public void setuIds(List<String> uIds) {
        this.uIds = uIds;
    }

    public List<String> getDayIds() {
        return dayIds;
    }

    public void setDayIds(List<String> dayIds) {
        this.dayIds = dayIds;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public String getKey() {
        return id;
    }
}
