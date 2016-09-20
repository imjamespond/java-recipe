package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:40
 */
@Desc("农场动态留言")
public class FarmActionReq extends BaseReq {
    @Desc("留言的好友Id")
    private String fid;
    @Desc("留言时候的内容")
    private String content;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
