package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 上午10:57
 */
@Desc("订单信息")
public class FarmOrderInfoVO {
    @Desc("剩余订单数量")
    private int surplusNum;
    @Desc("可以完成的所有订单数量")
    private int allNum;
    @Desc("订单列表")
    private OneOrderVO [] orderVOList;
    @Desc("订单下次到达时间")
    private long nextTime;
    @Desc("快速货运添加的 百分比")
    private int addPercent;


    public int getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(int surplusNum) {
        this.surplusNum = surplusNum;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }


    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public OneOrderVO[] getOrderVOList() {
        return orderVOList;
    }

    public void setOrderVOList(OneOrderVO[] orderVOList) {
        this.orderVOList = orderVOList;
    }

    public int getAddPercent() {
        return addPercent;
    }

    public void setAddPercent(int addPercent) {
        this.addPercent = addPercent;
    }
}
