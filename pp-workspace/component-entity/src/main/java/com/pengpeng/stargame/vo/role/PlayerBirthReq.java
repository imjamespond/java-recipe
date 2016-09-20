package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

import java.util.Date;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("生日设置")
public class PlayerBirthReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("生日")
    private long birth;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }
}
