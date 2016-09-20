package com.pengpeng.stargame.model.lottery;

import java.util.Date;
import java.util.List;

/**
 * @auther james
 * @since: 13-6-3下午5:27
 */
public class Lottery {

    //奖品
    private String itemId;
    //数量
    private int num;
    //奖品类型
    private int type;
    //奖品等级
    private int groupId;
    //游戏币
    private int gameCoin;
    //未中奖品
    private List<LotteryItem> lotteryItem;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<LotteryItem> getLotteryItem() {
        return lotteryItem;
    }

    public void setLotteryItem(List<LotteryItem> lotteryItem) {
        this.lotteryItem = lotteryItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }
}
