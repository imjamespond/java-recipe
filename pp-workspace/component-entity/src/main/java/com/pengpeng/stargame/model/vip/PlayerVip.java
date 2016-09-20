package com.pengpeng.stargame.model.vip;

import com.pengpeng.stargame.model.BaseEntity;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午4:37
 */
public class PlayerVip extends BaseEntity<String>{
    private String pid;
    //是否是VIp 1 是 0 不是
    private int isViP;
    //vip 结束日期
    private Date endTime;
    //VIp等级
    private int level;
    //预留，如果网站这边 有VIP体验卡 使用
    private int hours;
    //游戏这边 使用 VIp道具的 开始时间
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getViP() {
        return isViP;
    }

    public void setViP(int viP) {
        isViP = viP;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
