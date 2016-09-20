package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

import java.util.Date;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("称号设置")
public class PlayerTitleReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("称号")
    private String title;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
