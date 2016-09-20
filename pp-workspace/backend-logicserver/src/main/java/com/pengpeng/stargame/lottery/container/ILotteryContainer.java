package com.pengpeng.stargame.lottery.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.lottery.rule.LotteryRule;
import com.pengpeng.stargame.model.lottery.Lottery;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.lottery.RouletteVO;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface ILotteryContainer extends IMapContainer<String,LotteryRule> {

    /**
     * 抽奖并返回奖品
     * @param type 抽奖类型
     * @return
     */
    public Lottery lotteryDraw(int type,int sex);

    void addElementAndSort(LotteryRule rule);

    void addRouletteList(String item, String name, int num, int type, int probability, int gender, int speaker);

    RouletteVO rouletteDraw(Player player, PlayerLottery pl) throws AlertException;

    void deduct(Player player, PlayerLottery pl) throws AlertException;

    int rouletteAccept(Player player, PlayerLottery pl) throws AlertException;
}
