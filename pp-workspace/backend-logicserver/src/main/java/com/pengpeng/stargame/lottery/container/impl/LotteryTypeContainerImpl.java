package com.pengpeng.stargame.lottery.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.lottery.container.ILotteryTypeContainer;
import com.pengpeng.stargame.lottery.rule.LotteryRule;
import com.pengpeng.stargame.lottery.rule.LotteryTypeRule;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class LotteryTypeContainerImpl extends HashMapContainer<String, LotteryTypeRule> implements ILotteryTypeContainer {

    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @Override
    public void check(int type,Player player,PlayerLottery playerLottery) throws AlertException{

        if(type == LotteryRule.TYPE_FREE){
            if(playerLottery.getNum()<1){
                exceptionFactory.throwAlertException("lottery.over.limit");
            }
            playerLottery.deNum(1);
            playerLottery.setRefreshTime(new Date());
        }else if(type == LotteryRule.TYPE_GOLD){
            LotteryTypeRule ltr = this.items.get(Integer.toString(type));
            if ( player.getGoldCoin()<ltr.getCost()){
                exceptionFactory.throwAlertException("lottery.coin.notenough");
            }
            //player.decGoldCoin(ltr.getCost());
            playerRuleContainer.decGoldCoin(player,ltr.getCost(), PlayerConstant.GOLD_ACTION_25);
        }

        if(!this.items.containsKey(Integer.toString(type))){
            exceptionFactory.throwAlertException("lottery.type.notexist");
        }

    }
}


