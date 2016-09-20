package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-12-31
 * Time: 下午2:53
 */
@Desc("春节活动VO")
@EventAnnotation(name="event.springevent.update",desc="春节放鞭炮VO")
public class SpringEventVO {
    @Desc("1 表示放鞭炮进度未到达  2表示已经达到")
    private int status;
    @Desc("进度的最大值")
    private int maxValue;
    @Desc("进度的当前值")
    private int value;
    @Desc("如果是 status 2 ，倒计时剩余时间")
    private long surplusTime;
    @Desc("1 表示是开始放了")
    private int start;
    @Desc("家族Id 家族Id一样更新放鞭炮进度")
    private String familyId;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(long surplusTime) {
        this.surplusTime = surplusTime;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
