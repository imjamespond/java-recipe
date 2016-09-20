package com.pengpeng.stargame.vo.farm.decorate;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.room.DecorateVO;

/**
 * User: mql
 * Date: 14-3-14
 * Time: 下午2:29
 */
@Desc("农场装扮Vo")
@EventAnnotation(name="event.farm.decorate.update",desc="农场装饰数据更新")
public class FarmDecorateVO {
    @Desc("房间所有人的Id")
    private String pid;
    @Desc("土地的价格 基数")
    private int fieldGold;
    @Desc("土地的购买次数")
    private int buyFieldNum;
    @Desc("房间内的装饰品列表")
    private DecorateVO [] decorateVOs;
    @Desc("右下角 提示信息")
    private String [] hints;
    @Desc("最大可以购买的土地的数量")
    private int maxBuyFieldNum;
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public DecorateVO[] getDecorateVOs() {
        return decorateVOs;
    }

    public void setDecorateVOs(DecorateVO[] decorateVOs) {
        this.decorateVOs = decorateVOs;
    }

    public int getFieldGold() {
        return fieldGold;
    }

    public void setFieldGold(int fieldGold) {
        this.fieldGold = fieldGold;
    }

    public int getBuyFieldNum() {
        return buyFieldNum;
    }

    public void setBuyFieldNum(int buyFieldNum) {
        this.buyFieldNum = buyFieldNum;
    }

    public String[] getHints() {
        return hints;
    }

    public void setHints(String[] hints) {
        this.hints = hints;
    }

    public int getMaxBuyFieldNum() {
        return maxBuyFieldNum;
    }

    public void setMaxBuyFieldNum(int maxBuyFieldNum) {
        this.maxBuyFieldNum = maxBuyFieldNum;
    }
}
