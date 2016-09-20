package com.pengpeng.stargame.vo.antiaddiction;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: jms
 * Date: 13-10-9
 * Time: 下午4:37
 */
@Desc("实名认证的请求")
public class AntiAddictionReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("玩家名字")
    private String name;
    @Desc("身份证号码")
    private String identity;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
