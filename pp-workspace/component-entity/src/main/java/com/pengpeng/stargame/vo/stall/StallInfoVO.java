package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.vo.smallgame.PlayerSmallGameRankVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("摆摊信息")
public class StallInfoVO {

    @Desc("摆摊开启")
    private Boolean enable;     //
    @Desc("达人币开启")
    private Boolean needGold;     //
    @Desc("上架时间")
    private Long hitShelfTime;     //
    @Desc("可上架次数")
    private int hitShelfNum;     //
    @Desc("玩家货架")
    private StallPlayerShelfVO[] shelfs;
    @Desc("达人币数开通货架")
    private int goldShelf;     //
    @Desc("加好友数开通货架")
    private int friendShelf;     //
    @Desc("今日购买次数")
    private int buyingTimes;     //
    @Desc("农场助手开启剩余时间")
    private long assistant;     //
    @Desc("农场助手冷却时间")
    private long assCoolDown;     //
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Long getHitShelfTime() {
        return hitShelfTime;
    }

    public void setHitShelfTime(Long hitShelfTime) {
        this.hitShelfTime = hitShelfTime;
    }

    public int getHitShelfNum() {
        return hitShelfNum;
    }

    public void setHitShelfNum(int hitShelfNum) {
        this.hitShelfNum = hitShelfNum;
    }

    public StallPlayerShelfVO[] getShelfs() {
        return shelfs;
    }

    public void setShelfs(StallPlayerShelfVO[] shelfs) {
        this.shelfs = shelfs;
    }

    public int getGoldShelf() {
        return goldShelf;
    }

    public void setGoldShelf(int goldShelf) {
        this.goldShelf = goldShelf;
    }

    public int getFriendShelf() {
        return friendShelf;
    }

    public void setFriendShelf(int friendShelf) {
        this.friendShelf = friendShelf;
    }

    public Boolean getNeedGold() {
        return needGold;
    }

    public void setNeedGold(Boolean needGold) {
        this.needGold = needGold;
    }

    public int getBuyingTimes() {
        return buyingTimes;
    }

    public void setBuyingTimes(int buyingTimes) {
        this.buyingTimes = buyingTimes;
    }

    public void setBuyingTimes2(int buyingTimes, boolean vip) {
        if(vip)
        this.buyingTimes = StallConstant.BUYING_VIP_NUM-buyingTimes;
        else
        this.buyingTimes = StallConstant.BUYING_NUM-buyingTimes;
    }

    public long getAssistant() {
        return assistant;
    }

    public void setAssistant(long assistant) {
        this.assistant = assistant;
    }

    public long getAssCoolDown() {
        return assCoolDown;
    }

    public void setAssCoolDown(long assCoolDown) {
        this.assCoolDown = assCoolDown;
    }
}
