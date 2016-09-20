package com.pengpeng.stargame.vo.wharf;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("码头达人币")
public class WharfGoldCoinVO {

    @Desc("达人币")
    private int goldCoin;

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }
}
