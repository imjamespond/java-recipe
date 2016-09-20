package com.pengpeng.stargame.vo.lucky.tree;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("招财树的请求")
public class LuckyTreeReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;



    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
