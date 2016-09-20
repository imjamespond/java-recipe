package com.pengpeng.stargame.vo.success;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午2:59
 */
@Desc("成就请求")
public class SuccessReq extends BaseReq {
    @Desc("成就Id")
    private String sId;

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }
}
