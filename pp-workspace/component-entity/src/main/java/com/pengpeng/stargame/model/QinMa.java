package com.pengpeng.stargame.model;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-14上午10:16
 */
public class QinMa extends BaseEntity<String>{
    //亲妈id
    private String id;
    //农场好评
    private int farmEvaluation;
    //房间好评
    private int roomEvaluation;
    //清零时间
    private Date refreshTime;

    public QinMa(){

    }
    public  QinMa(String id,Date  date){
        this.id=id;
        this.refreshTime=date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFarmEvaluation() {
        return farmEvaluation;
    }

    public void setFarmEvaluation(int farmEvaluation) {
        this.farmEvaluation = farmEvaluation;
    }

    public int getRoomEvaluation() {
        return roomEvaluation;
    }

    public void setRoomEvaluation(int roomEvaluation) {
        this.roomEvaluation = roomEvaluation;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public void incFarmEvaluation(int val){
        farmEvaluation +=Math.abs(val);
    }
    public void incRoomEvaluation(int val){
        roomEvaluation +=Math.abs(val);
    }

    @Override
    public String getKey() {
        return id;
    }
}
