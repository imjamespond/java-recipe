package com.pengpeng.stargame.model.integral;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:16
 */
public class PlayerIntegralShow  extends BaseEntity<String> {
    private String pid;
    private Date nextTime;
    private List<IntegralAction> integralActionList;


    public void addAction(IntegralAction integralAction){
        integralActionList.add(integralAction);
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public List<IntegralAction> getIntegralActionList() {
        return integralActionList;
    }

    public void setIntegralActionList(List<IntegralAction> integralActionList) {
        this.integralActionList = integralActionList;
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
