package com.pengpeng.stargame.vo.vip;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午5:12
 */
@Desc("VIp特权VO")
public class VipPrivilegeVO {
    @Desc("类型")
    private int type;
    @Desc("功能数值")
    private int value;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
