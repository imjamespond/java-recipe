package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("小摊路人请求")
public class StallPassengerReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("序数0-3")
    private int passengerId;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }
}
