package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther james
 * @since: 14-5-16上午11:34
 */
@Desc("泡泡请求")
public class BubbleReq extends BaseReq {
    @Desc("pid")
    private String pid;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
