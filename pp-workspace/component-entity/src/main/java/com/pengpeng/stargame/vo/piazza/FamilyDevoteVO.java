package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-11下午4:45
 */
@Desc("家族贡献比例")
public class FamilyDevoteVO {
    @Desc("家族id")
    private String id;
    @Desc("游戏币贡献兑换比例")
    private int gameCoinDevote;
    @Desc("金币贡献兑换比例")
    private int goldCoinDevote;
    @Desc("游戏币经费兑换比例")
    private int gameCoinFunds;
    @Desc("金币经费兑换比例")
    private int goldCoinFunds;
    @Desc("道具贡献比例")
    private int propsDevote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGameCoinDevote() {
        return gameCoinDevote;
    }

    public void setGameCoinDevote(int gameCoinDevote) {
        this.gameCoinDevote = gameCoinDevote;
    }

    public int getGoldCoinDevote() {
        return goldCoinDevote;
    }

    public void setGoldCoinDevote(int goldCoinDevote) {
        this.goldCoinDevote = goldCoinDevote;
    }

    public int getGameCoinFunds() {
        return gameCoinFunds;
    }

    public void setGameCoinFunds(int gameCoinFunds) {
        this.gameCoinFunds = gameCoinFunds;
    }

    public int getGoldCoinFunds() {
        return goldCoinFunds;
    }

    public void setGoldCoinFunds(int goldCoinFunds) {
        this.goldCoinFunds = goldCoinFunds;
    }

    public int getPropsDevote() {
        return propsDevote;
    }

    public void setPropsDevote(int propsDevote) {
        this.propsDevote = propsDevote;
    }
}
