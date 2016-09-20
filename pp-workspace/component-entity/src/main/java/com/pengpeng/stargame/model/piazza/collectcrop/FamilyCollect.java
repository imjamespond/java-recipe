package com.pengpeng.stargame.model.piazza.collectcrop;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午4:02
 * 家族收集活动 实体，以家族为单位
 */
public class FamilyCollect extends BaseEntity<String> {
    private String fid;
    private String ruleId; //对应收集表的Id
    private int num;  //家族完成的数量
    private Date finishTime; //完成时间
    private Date startTime;//收集开始的日期
    private int ranking; //排行

    public FamilyCollect() {
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

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
