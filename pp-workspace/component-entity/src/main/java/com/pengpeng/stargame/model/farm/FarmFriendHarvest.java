package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;

/**
 * User: mql
 * Date: 13-4-28
 * Time: 上午11:33
 */
public class FarmFriendHarvest extends BaseEntity<String> {
    private String pId;
    private Date nextTime;
    //今天帮好友采摘次数
    private int num;
    //好友帮忙收获次数
    private int friendNum;
    //周 好友帮忙收获次数
    private int weekFriendNum;
    //周 清理时间
    private Date weekTime;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String getId() {
        return pId;
    }

    @Override
    public void setId(String id) {
       this.pId=id;
    }

    @Override
    public String getKey() {
        return this.pId;
    }

    public int getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(int friendNum) {
        this.friendNum = friendNum;
    }

    public boolean check(Date date){
        if(DateUtil.getDayOfYear(nextTime)!=DateUtil.getDayOfYear(date)){
            this.setNum(0);
            this.setFriendNum(0);
            this.setNextTime(date);
            return true;
        }
        if(weekTime==null){
            weekTime=date;
        }
        if(DateUtil.getWeekOfYear(weekTime)!=DateUtil.getWeekOfYear(date)){
            weekFriendNum=0;
            weekTime=date;
            return true;
        }
        return false;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public int getWeekFriendNum() {
        return weekFriendNum;
    }

    public void setWeekFriendNum(int weekFriendNum) {
        this.weekFriendNum = weekFriendNum;
    }

    public Date getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(Date weekTime) {
        this.weekTime = weekTime;
    }
}
