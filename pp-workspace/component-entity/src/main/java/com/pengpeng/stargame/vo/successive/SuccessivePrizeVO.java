package com.pengpeng.stargame.vo.successive;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("连续登陆奖励")
public class SuccessivePrizeVO {
	@Desc("连续登陆天数")
	private int day;
    @Desc("奖品列表")
    private SuccessiveItemVO[] itemVO;
    @Desc("连续登陆天数")
    private int goldCoin;
    @Desc("连续登陆天数")
    private int gameCoin;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public SuccessiveItemVO[] getItemVO() {
        return itemVO;
    }

    public void setItemVO(SuccessiveItemVO[] itemVO) {
        this.itemVO = itemVO;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }
}
