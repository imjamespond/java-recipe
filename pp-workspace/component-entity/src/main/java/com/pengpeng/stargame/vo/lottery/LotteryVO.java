package com.pengpeng.stargame.vo.lottery;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("抽奖返回信息")
public class LotteryVO {
	@Desc("奖品")
	private LotteryGoodsVO giftVO;
    @Desc("未中奖品列表")
    private LotteryGoodsVO[] goodsVOs;
    @Desc("奖品信息")
    private LotteryInfoVO lotteryInfoVO;

    public LotteryGoodsVO getGiftVO() {
        return giftVO;
    }

    public void setGiftVO(LotteryGoodsVO giftVO) {
        this.giftVO = giftVO;
    }

    public LotteryGoodsVO[] getGoodsVOs() {
        return goodsVOs;
    }

    public void setGoodsVOs(LotteryGoodsVO[] goodsVOs) {
        this.goodsVOs = goodsVOs;
    }

    public LotteryInfoVO getLotteryInfoVO() {
        return lotteryInfoVO;
    }

    public void setLotteryInfoVO(LotteryInfoVO lotteryInfoVO) {
        this.lotteryInfoVO = lotteryInfoVO;
    }
}
