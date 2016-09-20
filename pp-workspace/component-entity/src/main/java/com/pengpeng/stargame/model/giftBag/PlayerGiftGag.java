package com.pengpeng.stargame.model.giftBag;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 上午11:51
 */
public class PlayerGiftGag extends BaseEntity<String> {

   private String pid;
   private Map<String,String> dayGet;
   private Map<String ,String> historyGet;
    private Date nextTime;

    public PlayerGiftGag(){

    }
    public PlayerGiftGag(String pid){
         this.pid=pid;
        this.dayGet=new HashMap<String, String>();
        this.historyGet=new HashMap<String, String>();
        nextTime= DateUtil.getNextCountTime();
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Map<String, String> getDayGet() {
        return dayGet;
    }

    public void setDayGet(Map<String, String> dayGet) {
        this.dayGet = dayGet;
    }

    public Map<String, String> getHistoryGet() {
        return historyGet;
    }

    public void setHistoryGet(Map<String, String> historyGet) {
        this.historyGet = historyGet;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
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
}
