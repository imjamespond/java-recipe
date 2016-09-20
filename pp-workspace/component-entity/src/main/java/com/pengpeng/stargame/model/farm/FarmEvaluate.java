package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 农场 评价 信息
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:37
 */
public class FarmEvaluate extends BaseEntity<String> {
    private String pId;
    /**
     * 玩家农场  好评数量
     */
    private int num;
    /**
     * 玩家评价了 哪些好友的 农场，存 好友id
     */
    private List<String> friendIds;
    /**
     * 清空 时间 ，每天 0 点，清空 好评数量 跟 评价了好友的数量
     */
    private Date nextTime;
     //周好评数量
    private int weekNum;
    //周清理时间
    private Date weekTime;

    public FarmEvaluate(){
        friendIds = new ArrayList<String>();
    }
    public FarmEvaluate(String pid){
        this.pId = pid;
        friendIds = new ArrayList<String>();
    }
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
    public void incNum(int val){
        weekNum+=val;
        num+=val;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
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
        return pId;
    }

    public boolean check(long time){
        if (nextTime.getTime()<time){
            this.num=0;
            this.getFriendIds().clear();
            return true;
        }
        Date now=new Date();
        if(weekTime==null){
            weekTime=now;
        }
        if(DateUtil.getWeekOfYear(weekTime)!=DateUtil.getWeekOfYear(now)){
            weekNum=0;
            weekTime=now;
            return true;
        }
        return false;
    }
    public boolean isEvaluate(String fid){
       return friendIds.contains(fid);
    }

    public void evaluate(String fid){
        if (isEvaluate(fid)){
            return;
        }
        friendIds.add(fid);
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public Date getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(Date weekTime) {
        this.weekTime = weekTime;
    }
}
