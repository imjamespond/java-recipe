package com.pengpeng.stargame.model.gameEvent;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * User: mql
 * Date: 13-12-31
 * Time: 下午3:28
 */
public class FamilyEventValue extends BaseEntity<String> {
    private String familyId;
    /**
     * 家族鞭炮进度值
     */
    private int  firecrackerValue;
    /**
     * 状态 1未到达  2已经达到了
     */
    private int status;

    /**
     * 放鞭炮的时间
     *
     */
    private Date time ;

    /**
     * 添加 放鞭炮进度值
     * @param num
     */
    public void addFValue(int num){
        firecrackerValue+=num;
    }

    public void subFValue(int num){
        firecrackerValue-=num;
        if(firecrackerValue<=0){
            firecrackerValue=0;
        }
    }
    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getFirecrackerValue() {
        return firecrackerValue;
    }

    public void setFirecrackerValue(int firecrackerValue) {
        this.firecrackerValue = firecrackerValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String getId() {
        return familyId;
    }

    @Override
    public void setId(String id) {

    }



    @Override
    public String getKey() {
        return familyId;
    }
}
