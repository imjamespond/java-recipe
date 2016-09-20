package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

import java.util.Date;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("签名设置")
public class PlayerSignatureReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("签名")
    private String signature;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
