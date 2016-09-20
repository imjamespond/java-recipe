package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-1-20
 * Time: 下午2:31
 */
@Desc("放气球面板信息VO")
public class BalloonPanelInfoVO {
    @Desc("气球列表 BalloonVO 对象数组")
    private BalloonVO [] balloonVOs;
    @Desc("时间数组")
    private int [] times;
    @Desc("时间数组 对应的 消耗 游戏币")
    private int [] gameCoin;

    public BalloonVO[] getBalloonVOs() {
        return balloonVOs;
    }

    public void setBalloonVOs(BalloonVO[] balloonVOs) {
        this.balloonVOs = balloonVOs;
    }

    public int[] getTimes() {
        return times;
    }

    public void setTimes(int[] times) {
        this.times = times;
    }

    public int[] getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int[] gameCoin) {
        this.gameCoin = gameCoin;
    }
}
