package com.pengpeng.stargame.model.room;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 房间 评价 信息
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:37
 */
public class RoomEvaluate extends BaseEntity<String> {
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

    public RoomEvaluate(){
        friendIds = new ArrayList<String>();
    }
    public RoomEvaluate(String pid){
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

}
