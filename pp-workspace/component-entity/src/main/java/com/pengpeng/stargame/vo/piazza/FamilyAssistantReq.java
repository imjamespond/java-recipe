package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: james
 * Date: 13-11-28
 * Time: 下午4:37
 */
@Desc("明星助理的请求")
public class FamilyAssistantReq extends BaseReq {

    @Desc("pId")
    private String pid;
    @Desc("家族的Id")
    private String fid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
