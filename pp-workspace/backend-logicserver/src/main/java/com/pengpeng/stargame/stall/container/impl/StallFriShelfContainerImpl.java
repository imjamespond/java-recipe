package com.pengpeng.stargame.stall.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.model.stall.PlayerShelf;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.stall.container.IStallFriShelfContainer;
import com.pengpeng.stargame.stall.rule.StallFriShelfRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class StallFriShelfContainerImpl extends HashMapContainer<String, StallFriShelfRule> implements IStallFriShelfContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;


    @Override
    public int getFriend(int shelfNum) {
        StallFriShelfRule rule = getElement(String.valueOf(shelfNum));
        if(null==rule){
            return 0;
        }
        return rule.getFriNum();
    }

    @Override
    public void enable(PlayerStall ps, Friend f) throws AlertException {
        Map<String,FriendItem> fm = f.getFriends();
        if(null==fm){
            return;
        }
        int needNum = 0;
        StallFriShelfRule rule = getElement(String.valueOf(ps.getPlayerFriShelf().size()));
        if(null==rule){
            return;
            //exceptionFactory.throwAlertException("invalid.rule");
        }
        needNum = rule.getFriNum();
        if(needNum<=fm.size()){
            ps.getPlayerFriShelf().add(new PlayerShelf());
        }
    }
}

