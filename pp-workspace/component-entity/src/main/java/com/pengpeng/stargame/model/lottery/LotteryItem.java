package com.pengpeng.stargame.model.lottery;

import java.util.List;

/**
 * @auther james
 * @since: 13-6-3下午5:27
 */
public class LotteryItem {

    //物品
    private String itemId;
    //数量
    private int num;
    //游戏币
    private int gameCoin;

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

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }
}
