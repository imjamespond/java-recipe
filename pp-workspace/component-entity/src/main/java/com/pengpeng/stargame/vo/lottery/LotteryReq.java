package com.pengpeng.stargame.vo.lottery;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("抽奖的请求")
public class LotteryReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("抽奖的类型1为达人币2为免费")
    private int type;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
