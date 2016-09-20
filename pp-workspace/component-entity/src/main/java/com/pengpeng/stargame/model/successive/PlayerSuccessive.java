package com.pengpeng.stargame.model.successive;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerSuccessive extends BaseEntity<String> {
    private String pid;
    private int day;
    private Date lastLogin;
    private int getPrize;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getGetPrize() {
        return getPrize;
    }

    public void setGetPrize(int getPrize) {
        this.getPrize = getPrize;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;
    }

    public void deNum(int num){
        this.day-=num;
    }

    public void incDay(int num){
        this.day+=num;
    }
}
