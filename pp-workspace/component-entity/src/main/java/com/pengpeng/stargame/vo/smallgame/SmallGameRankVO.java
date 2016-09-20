package com.pengpeng.stargame.vo.smallgame;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("小游戏排行")
public class SmallGameRankVO {
	@Desc("小游戏类型")
	private int type;
    @Desc("小游戏名称")
    private String name;
    @Desc("小游戏购买")
    private String price;
    @Desc("玩家好友排行")
    private PlayerSmallGameRankVO[] friendPlayerSmallGameRankVOs;
    @Desc("本周玩家排行")
    private PlayerSmallGameRankVO[] weekPlayerSmallGameRankVOs;

    @Desc("本周本人排行")
    private int weekRank;
    @Desc("今日免费次数")
    private int freeTime;
    @Desc("达人币次数")
    private int goldTime;
    @Desc("增加粉丝值")
    private int fanVal;
    @Desc("历史最高分")
    private int maxScore;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerSmallGameRankVO[] getFriendPlayerSmallGameRankVOs() {
        return friendPlayerSmallGameRankVOs;
    }

    public void setFriendPlayerSmallGameRankVOs(PlayerSmallGameRankVO[] friendPlayerSmallGameRankVOs) {
        this.friendPlayerSmallGameRankVOs = friendPlayerSmallGameRankVOs;
    }

    public PlayerSmallGameRankVO[] getWeekPlayerSmallGameRankVOs() {
        return weekPlayerSmallGameRankVOs;
    }

    public void setWeekPlayerSmallGameRankVOs(PlayerSmallGameRankVO[] weekPlayerSmallGameRankVOs) {
        this.weekPlayerSmallGameRankVOs = weekPlayerSmallGameRankVOs;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public int getGoldTime() {
        return goldTime;
    }

    public void setGoldTime(int goldTime) {
        this.goldTime = goldTime;
    }



    public int getWeekRank() {
        return weekRank;
    }

    public void setWeekRank(int weekRank) {
        this.weekRank = weekRank+1;
    }

    public int getFanVal() {
        return fanVal;
    }

    public void setFanVal(int fanVal) {
        this.fanVal = fanVal;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

}
