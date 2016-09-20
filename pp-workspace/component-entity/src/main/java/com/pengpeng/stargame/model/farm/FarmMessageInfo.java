package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 农场留言信息
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:48
 */
public class FarmMessageInfo extends BaseEntity<String> {
    private String pid;
    //日留言数量
    private int dayNum;
    //日留言清理时间
    private Date date;
    //留言信息
    private List<FarmMessage> farmMessageList;
    //上次查看时间
    private Date lastNumTime;
    public FarmMessageInfo(){

    }

    public FarmMessageInfo(String pid) {
        this.pid = pid;
        farmMessageList=new ArrayList<FarmMessage>();
    }


    public int getNewNum(){
        List<FarmMessage> farmMessages=new ArrayList<FarmMessage>();
        for(FarmMessage farmMessage:farmMessageList){
            if(lastNumTime==null){
                farmMessages.add(farmMessage);
                continue;
            }
            if(farmMessage.getDate()==null){
                continue;
            }
            if(farmMessage.getDate().after(lastNumTime)){
                farmMessages.add(farmMessage);
            }
        }
        return farmMessages.size();
    }

    public boolean  check(Date now){
        boolean ischange=false;
        if(date==null){
            dayNum=0;
            date=now;
            ischange= true;
        }
        if(DateUtil.getDayOfYear(date)!=DateUtil.getDayOfYear(now)){
            dayNum=0;
            date=now;
            ischange= true;
        }
        List<FarmMessage> delete =new ArrayList<FarmMessage>();
        for(FarmMessage farmMessage:farmMessageList){
            if(DateUtil.getMonthOfYear(farmMessage.getDate())!=DateUtil.getMonthOfYear(new Date())){
                delete.add(farmMessage);
            }

        }
        if(delete.size()>0){
            farmMessageList.removeAll(delete);
            ischange= true;
        }
        return ischange;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getLastNumTime() {
        return lastNumTime;
    }

    public void setLastNumTime(Date lastNumTime) {
        this.lastNumTime = lastNumTime;
    }



    public void addMessage(FarmMessage message){
        dayNum++;
        farmMessageList.add(message);
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<FarmMessage> getFarmMessageList() {
        return farmMessageList;
    }

    public void setFarmMessageList(List<FarmMessage> farmMessageList) {
        this.farmMessageList = farmMessageList;
    }

    @Override
    public String getId() {
        return pid;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
