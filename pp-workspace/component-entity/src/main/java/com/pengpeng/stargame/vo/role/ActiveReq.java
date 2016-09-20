package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12下午12:17
 */
@Desc("活跃度积分领取")
public class ActiveReq extends BaseReq {
    @Desc("活跃度值")
    private int active;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
