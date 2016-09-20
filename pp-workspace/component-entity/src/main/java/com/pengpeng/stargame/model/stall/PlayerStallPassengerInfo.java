package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerStallPassengerInfo extends BaseEntity<String> {
    private String pid;
    private Date refreshDate;//刷新时间
    private PlayerStallPassenger[] passengers;     //路人
    private int credit;//每日累计积分数
    private Date creditDate;
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public PlayerStallPassenger[] getPassengers() {
        return passengers;
    }

    public void setPassengers(PlayerStallPassenger[] passengers) {
        this.passengers = passengers;
    }

    public Date getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Date getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate = creditDate;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;
    }

}
