package com.pengpeng.stargame.vo.integral;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午5:24
 */
@Desc("积分流水")
public class IntegralActionVO {
    @Desc("类型")
    private int type;
    @Desc("数量")
    private int num;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
