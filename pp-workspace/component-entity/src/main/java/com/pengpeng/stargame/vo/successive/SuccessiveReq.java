package com.pengpeng.stargame.vo.successive;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("连续登陆请求")
public class SuccessiveReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("第几天")
    private String day;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
