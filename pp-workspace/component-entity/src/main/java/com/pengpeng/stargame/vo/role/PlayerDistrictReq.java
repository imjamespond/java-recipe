package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("区域设置")
public class PlayerDistrictReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("省")
    private int province;
    @Desc("市")
    private int city;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }
}
