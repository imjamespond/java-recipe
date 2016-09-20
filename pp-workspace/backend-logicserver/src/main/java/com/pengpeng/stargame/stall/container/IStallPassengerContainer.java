package com.pengpeng.stargame.stall.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.stall.PlayerStallPassenger;
import com.pengpeng.stargame.model.stall.PlayerStallPassengerInfo;
import com.pengpeng.stargame.stall.rule.StallPassengerFashionRule;
import com.pengpeng.stargame.stall.rule.StallPassengerPurchaseRule;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface IStallPassengerContainer {


    void addFashionRule(StallPassengerFashionRule rule) ;
    void addPurchaseRule(StallPassengerPurchaseRule rule) ;

    boolean check(PlayerStallPassengerInfo info, FarmPlayer fp);

    StallPassengerPurchaseRule getPurchaseRules(String key) throws AlertException ;

    StallPassengerFashionRule getFashionRules(String key) throws AlertException;

    PlayerStallPassenger getPlayerStallPassenger(PlayerStallPassengerInfo info, int id) throws AlertException;
}
