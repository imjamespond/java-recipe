package com.pengpeng.stargame.model.integral;

import java.util.Date;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:19
 */
public class IntegralAction {
    //类型
    private int type;
    //数量
    private int num;
    //获取日期
    private Date time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
