package com.pengpeng.stargame.model.piazza.collectcrop;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.*;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:41
 */
public class FamilyMailControl extends BaseEntity<String> {
    private String fid;
    //Date 为发的时候的日期 ，String为 玩家收集排序Set，根据这个Set发积分奖励
    private Map<String,Date> mailRewads;
    private Map<String ,Integer> num;//记录本次收集活动 需要收集的总量 计算百分比
    private Map<String ,Integer> allIntegral;//本次奖励多少积分

    public FamilyMailControl(){
        mailRewads=new HashMap<String, Date>();
        num=new HashMap<String, Integer>();
        allIntegral=new HashMap<String, Integer>();
    }
    public Set<String> check(){
        Date now=new Date();
        boolean  change=false;
        Set<String> set=new HashSet<String>();
        for(Map.Entry<String,Date> entry:mailRewads.entrySet()){
            if(((now.getTime()-entry.getValue().getTime())/(24*60*60*1000))>30){ //保存30天邮件
                set.add(entry.getKey());
            }
        }
        for(String key:set){
            mailRewads.remove(key);
            num.remove(key);
            allIntegral.remove(key);

        }
        return set;


    }
    public void addMailRewards(String key,Date date){
        mailRewads.put(key,date);
    }
    public void addControlNum(String key,int num){
        this.num.put(key,num);
    }
    public void addAllIntegral(String key,int num){
        this.allIntegral.put(key,num);
    }
    public Map<String, Date> getMailRewads() {
        return mailRewads;
    }

    public void setMailRewads(Map<String, Date> mailRewads) {
        this.mailRewads = mailRewads;
    }

    public Map<String, Integer> getNum() {
        return num;
    }

    public void setNum(Map<String, Integer> num) {
        this.num = num;
    }

    public String getfId() {
        return fid;
    }

    public void setfId(String fId) {
        this.fid = fId;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Map<String, Integer> getAllIntegral() {
        return allIntegral;
    }

    public void setAllIntegral(Map<String, Integer> allIntegral) {
        this.allIntegral = allIntegral;
    }

    @Override
    public String getId() {
        return fid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return fid;
    }
}
