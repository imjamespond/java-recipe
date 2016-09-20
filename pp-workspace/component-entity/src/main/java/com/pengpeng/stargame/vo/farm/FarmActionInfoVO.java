package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:19
 */
@Desc("农场动态VO")
public class FarmActionInfoVO {
    @Desc("总浏览量")
    private int allVisite;
    @Desc("今日浏览量")
    private int dayVisite;

    @Desc("今日好评数量")
    private int goodReputation;
    @Desc("日好友帮忙收获的次数")
    private int friendHelpNum;
    @Desc("周好评数量")
    private int weekGoodReputation;
    @Desc("周好友帮忙收获的次数")
    private int weekFriendHelpNum;

    @Desc("动态列表  FarmActionVO 类型数组")
    private FarmActionVO[]  farmActionVOs;

    public int getAllVisite() {
        return allVisite;
    }

    public void setAllVisite(int allVisite) {
        this.allVisite = allVisite;
    }

    public int getDayVisite() {
        return dayVisite;
    }

    public void setDayVisite(int dayVisite) {
        this.dayVisite = dayVisite;
    }

    public FarmActionVO[] getFarmActionVOs() {
        return farmActionVOs;
    }

    public void setFarmActionVOs(FarmActionVO[] farmActionVOs) {
        this.farmActionVOs = farmActionVOs;
    }

    public int getGoodReputation() {
        return goodReputation;
    }

    public void setGoodReputation(int goodReputation) {
        this.goodReputation = goodReputation;
    }

    public int getFriendHelpNum() {
        return friendHelpNum;
    }

    public void setFriendHelpNum(int friendHelpNum) {
        this.friendHelpNum = friendHelpNum;
    }

    public int getWeekGoodReputation() {
        return weekGoodReputation;
    }

    public void setWeekGoodReputation(int weekGoodReputation) {
        this.weekGoodReputation = weekGoodReputation;
    }

    public int getWeekFriendHelpNum() {
        return weekFriendHelpNum;
    }

    public void setWeekFriendHelpNum(int weekFriendHelpNum) {
        this.weekFriendHelpNum = weekFriendHelpNum;
    }
}
