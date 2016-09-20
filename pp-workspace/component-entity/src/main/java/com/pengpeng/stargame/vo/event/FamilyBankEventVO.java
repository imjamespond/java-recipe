package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-12-23
 * Time: 上午11:03
 */
@Desc("家族银行活动的VO")
public class FamilyBankEventVO {
    @Desc("领取的时候 领取了多少达人币")
    private int gold;
    @Desc("家族存款")
    private int gameMoney;
    @Desc("活动的奖励信息 说明 ：游戏币，达人币，状态（0未达到 1已经领取 2可以领取）;")
    private String info;

    public int getGameMoney() {
        return gameMoney;
    }

    public void setGameMoney(int gameMoney) {
        this.gameMoney = gameMoney;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
