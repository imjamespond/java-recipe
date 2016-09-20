package com.pengpeng.stargame.model.gameEvent;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 13-12-23
 * Time: 上午9:24
 */

/**
 * 记录 玩家活动的 一些状态数据
 */
public class PlayerEvent extends BaseEntity<String> {
    private String pid;
    //玩家拾取的 掉落信息 ，活动Id 次数  每日有限制
    private Map<String ,Integer> dropInfo;
    //下次清理的时间
    private Date nextTime;
    // 家族银行活动相关  Key活动的Id （预留到时候如果在上一样的活动，只用改ID） Value 为数据
    private Map<String ,FamilyBankEvent> familyBankEventMap;


    public PlayerEvent(){

    }

    public FamilyBankEvent getFamilyBankEvent(String eventId){
        if(familyBankEventMap.containsKey(eventId)){
            return familyBankEventMap.get(eventId);
        }else{
            FamilyBankEvent familyBankEvent=new FamilyBankEvent();
            familyBankEventMap.put(eventId,familyBankEvent);
            return familyBankEvent;
        }
    }

    public void updateFamilyBankEvent(String eventId,FamilyBankEvent familyBankEvent){
        familyBankEventMap.put(eventId,familyBankEvent);
    }
    public PlayerEvent(String pid){
        this.pid=pid;
        dropInfo=new HashMap<String, Integer>();
        familyBankEventMap=new HashMap<String, FamilyBankEvent>();
    }

    /**
     * 添加 每日 拾取场景里面的 掉落次数
     * @param eventId
     * @param num
     */
    public void addPickNum(String eventId,int num){
         if(dropInfo.containsKey(eventId)){
             dropInfo.put(eventId,dropInfo.get(eventId)+num);
         } else {
             dropInfo.put(eventId,num);
         }
    }

    /**
     * 获取 活动 的拾取次数
     * @param eventId
     */
    public Integer getPickNum(String eventId){
        if(dropInfo.containsKey(eventId)){
           return dropInfo.get(eventId);
        } else {
            return 0;
        }
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Map<String, Integer> getDropInfo() {
        return dropInfo;
    }

    public void setDropInfo(Map<String, Integer> dropInfo) {
        this.dropInfo = dropInfo;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public Map<String, FamilyBankEvent> getFamilyBankEventMap() {
        return familyBankEventMap;
    }

    public void setFamilyBankEventMap(Map<String, FamilyBankEvent> familyBankEventMap) {
        this.familyBankEventMap = familyBankEventMap;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pid;
    }
}
