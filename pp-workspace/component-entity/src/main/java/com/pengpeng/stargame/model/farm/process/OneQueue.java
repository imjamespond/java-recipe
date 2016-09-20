package com.pengpeng.stargame.model.farm.process;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午1:58
 */
public class OneQueue {
    /**
     * 队列的唯一Id
     */
    private String id;
    /**
     * 队列的生产Id
     */
    private String processid;

    /**
     * 状态  1新加 2进行中 3完成
     */
    private int status;
    /**
     * 队列完成需要的秒数
     */
    private int time;
    /**
     * 队列完成需要的秒数 (实际秒数 ，如果有加速Buffer 那么计算加速Buffer的时间)
     */
    private int speedTime;
    /**
     * 队列已经进行了多久，用于队列百分比显示 。当加速结束时间 介于这个队列的开始时间和结束时间之间时候 赋值
     */
    private int  alreadyTime;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSpeedTime() {
        if(speedTime==0){
            return time;
        }
        return speedTime;
    }

    public void setSpeedTime(int speedTime) {
        this.speedTime = speedTime;
    }

    public int getAlreadyTime() {
        return alreadyTime;
    }

    public void setAlreadyTime(int alreadyTime) {
        this.alreadyTime = alreadyTime;
    }
}

