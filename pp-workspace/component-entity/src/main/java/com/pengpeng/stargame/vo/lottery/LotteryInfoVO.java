package com.pengpeng.stargame.vo.lottery;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("抽奖信息")
public class LotteryInfoVO {
	@Desc("抽奖次数")
	private int num;
    @Desc("中奖列表")
    private OneLotteryInfoVO[] oneLotteryInfoVOs;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public OneLotteryInfoVO[] getOneLotteryInfoVOs() {
        return oneLotteryInfoVOs;
    }

    public void setOneLotteryInfoVOs(OneLotteryInfoVO[] oneLotteryInfoVOs) {
        this.oneLotteryInfoVOs = oneLotteryInfoVOs;
    }
}
