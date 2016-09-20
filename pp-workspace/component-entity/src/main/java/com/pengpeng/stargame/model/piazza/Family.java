package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.Date;

/**
 * User: mql
 * Date: 13-6-26
 * Time: 下午5:42
 */
public class Family extends BaseEntity<String> {
    private String  id;
    private int  funds;//家族经费
    private String name;
    private String qq;
    private String yy;
    private String notice;

    /**
     * 粉丝值
     */
    private int fansValue;
    /**
     * 周粉丝值 排行
     */
    private int weekFansValue;
    /**
     * 周粉丝值 清零时间
     */
    private Date weekTime;

    public boolean refresh(){
        boolean  change=false;
        Date now=new Date();
        if (weekTime==null){
            weekTime = now;
        }
        if (DateUtil.getWeekOfYear(weekTime)!=DateUtil.getWeekOfYear(now)){
           weekFansValue=0;
            weekTime=now;
            change=true;
        }
        return change;
    }

    public int getFansValue() {
        return fansValue;
    }

    public void setFansValue(int fansValue) {
        this.fansValue = fansValue;
    }
    public int getFunds() {
        return funds;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    @Override
    public String getId() {
       return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void incFunds(int funds) {
        this.funds +=Math.abs(funds);
    }

    public void decFunds(int funds) {
        this.funds -=Math.abs(funds);
    }
    public void incFansValue(int fansValue){
        this.fansValue+=Math.abs(fansValue);
        this.weekFansValue +=Math.abs(fansValue);
    }

    public int getWeekFansValue() {
        return weekFansValue;
    }

    public void setWeekFansValue(int weekFansValue) {
        this.weekFansValue = weekFansValue;
    }

    public Date getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(Date weekTime) {
        this.weekTime = weekTime;
    }
}
