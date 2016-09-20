package com.pengpeng.stargame.vo.successive;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("连续登陆信息")
public class SuccessiveInfoVO {
	@Desc("连续登陆天数")
	private int day;
    @Desc("是否已经领取")
    private Boolean[] getPrize;
    @Desc("奖品列表")
    private SuccessivePrizeVO[] prizeVO;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public SuccessivePrizeVO[] getPrizeVO() {
        return prizeVO;
    }

    public void setPrizeVO(SuccessivePrizeVO[] prizeVO) {
        this.prizeVO = prizeVO;
    }

    public Boolean[] getGetPrize() {
        return getPrize;
    }

    public void setGetPrize(Boolean[] getPrize) {
        this.getPrize = getPrize;
    }
}
