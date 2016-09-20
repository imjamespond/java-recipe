package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 农场动态信息
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:14
 */
public class FarmActionInfo extends BaseEntity<String> {
    private String pid;
    //总浏览量
    private int allVisite;
    //今日浏览量
    private int dayVisite;
    //今日浏览人
    private List<String> visiter;
    //刷新时间
    private Date nextTime;
    //动态列表
    private List<FarmAction> farmActionList;
    //上次查看时间
    private Date lastNumTime;

    public int getNewNum(){
        List<FarmAction> farmActions=new ArrayList<FarmAction>();
        for(FarmAction farmAction:farmActionList){
            if(lastNumTime==null){
                farmActions.add(farmAction);
                continue;
            }
            if(farmAction.getDate().after(lastNumTime)){
                farmActions.add(farmAction);
            }
        }
        return farmActions.size();
    }

    public void addAction(FarmAction farmAction){
        this.farmActionList.add(farmAction);
    }
    public FarmActionInfo(){

    }
    public FarmActionInfo(String pid){
        this.pid=pid;
        farmActionList=new ArrayList<FarmAction>();
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getAllVisite() {
        return allVisite;
    }

    public void setAllVisite(int allVisite) {
        this.allVisite = allVisite;
    }

    public int getDayVisite() {
        return dayVisite;
    }

    public void setDayVisite(int dayVisite) {
        this.dayVisite = dayVisite;
    }

    public List<String> getVisiter() {
        return visiter;
    }

    public void setVisiter(List<String> visiter) {
        this.visiter = visiter;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public List<FarmAction> getFarmActionList() {
        return farmActionList;
    }

    public void setFarmActionList(List<FarmAction> farmActionList) {
        this.farmActionList = farmActionList;
    }

    public Date getLastNumTime() {
        return lastNumTime;
    }

    public void setLastNumTime(Date lastNumTime) {
        this.lastNumTime = lastNumTime;
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
