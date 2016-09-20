package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:29
 */
@Desc("留言VO")
public class FarmMessageVO {
    @Desc("留言好友Id")
    private String fid;
    @Desc("头像")
    private String portrait;
    @Desc("好友名字")
    private String name;
    @Desc("发送时间 毫秒")
    private long date;
    @Desc("留言内容")
    private String content;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
