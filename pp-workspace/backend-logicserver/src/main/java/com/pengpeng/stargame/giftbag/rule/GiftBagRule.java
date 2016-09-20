package com.pengpeng.stargame.giftbag.rule;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;

import java.util.List;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 上午11:23
 */
@Entity
@Table(name = "sg_rule_giftbag")
public class GiftBagRule extends BaseEntity<String>{
    //任务编号
    @Id
    private String id;
    @Column //描述
    private String des;

    @Column //扩展用 1给物品
    private int type;
    @Column //礼包物品编辑
    private String rewardEditor;
    @Column  //开始时间
    private String startTime;
    @Column //结束如期
    private String endTime;
    @Transient
    //礼包内的物品列表
    List<ItemData> ItemDataList;

    public void init(){

        ItemDataList=new ArrayList<ItemData>();
       if(rewardEditor!=null&&!rewardEditor.equals("")){
           String s []= rewardEditor.split(";");
           for(String ss:s){
               String sss[] =ss.split(",");
               if(sss.length<2){
                   continue;
               }
               ItemData itemData=new ItemData();
               itemData.setItemId(sss[0]);
               itemData.setNum(Integer.parseInt(sss[1]));
               ItemDataList.add(itemData);
           }
       }

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRewardEditor() {
        return rewardEditor;
    }

    public void setRewardEditor(String rewardEditor) {
        this.rewardEditor = rewardEditor;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ItemData> getItemDataList() {
        return ItemDataList;
    }

    public void setItemDataList(List<ItemData> itemDataList) {
        ItemDataList = itemDataList;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return id;
    }


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
