package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: mql
 * Date: 13-7-2
 * Time: 下午4:01
 */
@Entity
@Table(name="sg_rule_activecontrol")
public class ActiveControlRule extends BaseEntity<String> {

    //活动编号
    @Id
   private String  activeId;
    //活动名称
    @Column
    private String  name;
    //开放等级要求
    @Column
    private int level;
    //参与贡献条件
    @Column
    private String devote ;
    //开启前5分钟活动公告
    @Column
    private String frontNotice;
    //开启时活动公告
    @Column
    private String openNotice;
    //开启日期
    @Column
    private String openDate;
    //开启时间
    @Column
    private String openTime;
    //开启后5分钟活动功能
    @Column
    private String afterNotice;
    //结束日期
    @Column
    private String endDate;
    //结束时间
    @Column
    private String endTime;
    //结束后活动公告
    @Column
    private String endNotice;
    //循环设置
    @Column
    private String cycleSettings;
    //活动内容说明
    @Column
    private String  contentDesc;
    //活动奖励说明
    @Column
    private String rewardDesc;
    //奖励到家显示
    @Column
    private String itemsDisplay;
    //举行时间
    @Column
    private String time;
    //活动场景坐标
    @Column
    private String sceneCoordinates;

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDevote() {
        return devote;
    }

    public void setDevote(String devote) {
        this.devote = devote;
    }

    public String getFrontNotice() {
        return frontNotice;
    }

    public void setFrontNotice(String frontNotice) {
        this.frontNotice = frontNotice;
    }

    public String getOpenNotice() {
        return openNotice;
    }

    public void setOpenNotice(String openNotice) {
        this.openNotice = openNotice;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getAfterNotice() {
        return afterNotice;
    }

    public void setAfterNotice(String afterNotice) {
        this.afterNotice = afterNotice;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndNotice() {
        return endNotice;
    }

    public void setEndNotice(String endNotice) {
        this.endNotice = endNotice;
    }

    public String getCycleSettings() {
        return cycleSettings;
    }

    public void setCycleSettings(String cycleSettings) {
        this.cycleSettings = cycleSettings;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc;
    }

    public String getItemsDisplay() {
        return itemsDisplay;
    }

    public void setItemsDisplay(String itemsDisplay) {
        this.itemsDisplay = itemsDisplay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSceneCoordinates() {
        return sceneCoordinates;
    }

    public void setSceneCoordinates(String sceneCoordinates) {
        this.sceneCoordinates = sceneCoordinates;
    }

    @Override
    public String getId() {
        return activeId;     }

    @Override
    public void setId(String id) {
           }

    @Override
    public String getKey() {
        return activeId;   }
}
