package com.pengpeng.stargame.model.piazza.collectcrop;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import java.util.Date;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:09
 * 收集控制实体  作用于所有家族
 */
public class CollectControl extends BaseEntity<String> {
    private Date nextTime; //下次开始时间
    private int ranking;//名次 累加 控制家族完成顺序

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
       return "";
    }
}
