package com.pengpeng.stargame.vo.lottery;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseRewardVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("轮盘返回信息")
public class RouletteVO {
	@Desc("奖品序号")
	private int reward;
    @Desc("奖品列表")
    private BaseRewardVO[] rewardVOs;
    @Desc("奖品记录")
    private RouletteHistVO[] infoVO;
    @Desc("轮盘次数")
    private int num;
    @Desc("轮盘经验")
    private int exp;

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public BaseRewardVO[] getRewardVOs() {
        return rewardVOs;
    }

    public void setRewardVOs(BaseRewardVO[] rewardVOs) {
        this.rewardVOs = rewardVOs;
    }

    public RouletteHistVO[] getInfoVO() {
        return infoVO;
    }

    public void setInfoVO(RouletteHistVO[] infoVO) {
        this.infoVO = infoVO;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
