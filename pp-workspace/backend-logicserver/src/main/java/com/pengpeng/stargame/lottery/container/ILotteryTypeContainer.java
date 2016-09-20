package com.pengpeng.stargame.lottery.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.lottery.rule.LotteryTypeRule;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.player.Player;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface ILotteryTypeContainer extends IMapContainer<String,LotteryTypeRule> {
    /**
     * 抽奖检测
     * @param type 抽奖类型
     * @return
     */
    public void check(int type,Player player,PlayerLottery playerLottery) throws AlertException;

}
