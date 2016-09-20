package com.pengpeng.stargame.model.piazza;

import java.util.Date;

/**
 * User: mql
 * Date: 13-7-1
 * Time: 上午9:37
 */
public class PlayerBlessing {
    private String pid;
    private int num; //已经祝福的次数
    private Date nextTime;//下次可以祝福的时间

    public PlayerBlessing(){

    }

    public PlayerBlessing(String pid){
        this.pid=pid;
        this.num=0;
        this.nextTime=new Date();
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }
}
