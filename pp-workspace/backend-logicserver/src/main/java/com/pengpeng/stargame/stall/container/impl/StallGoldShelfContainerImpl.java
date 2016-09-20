package com.pengpeng.stargame.stall.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.PlayerShelf;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.stall.container.IStallGoldShelfContainer;
import com.pengpeng.stargame.stall.rule.StallGoldShelfRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class StallGoldShelfContainerImpl extends HashMapContainer<String, StallGoldShelfRule> implements IStallGoldShelfContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;


    @Override
    public int getGold(int shelfNum) {
        StallGoldShelfRule rule = this.getElement(String.valueOf(shelfNum));
        if(null!=rule)
            return rule.getNeedGold();
        return 0;
    }

    @Override
    public void enable(PlayerStall ps,Player p) throws AlertException {
        List<PlayerShelf> shelfs = ps.getPlayerGoldShelf();
        int shelfNum = shelfs.size();
        int needGold = 0;
        StallGoldShelfRule rule = this.getElement(String.valueOf(shelfNum));
        if(null!=rule){
            needGold = rule.getNeedGold();
        }
        if(p.getGoldCoin()<needGold){
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
        //p.decGoldCoin(needGold);
        playerRuleContainer.decGoldCoin(p,needGold, PlayerConstant.GOLD_ACTION_40);
        ps.getPlayerGoldShelf().add(new PlayerShelf());
    }

}

