package com.pengpeng.stargame.stall.container.impl;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.stall.PlayerStallPassenger;
import com.pengpeng.stargame.model.stall.PlayerStallPassengerInfo;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.stall.container.IStallPassengerContainer;
import com.pengpeng.stargame.stall.rule.StallPassengerFashionRule;
import com.pengpeng.stargame.stall.rule.StallPassengerPurchaseRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class StallPassengerContainerImpl implements IStallPassengerContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    private static Map<String,StallPassengerFashionRule> fashionRules = new HashMap<String,StallPassengerFashionRule>();

    private static Map<String,StallPassengerPurchaseRule> purchaseRules = new HashMap<String,StallPassengerPurchaseRule>();



    @Override
    public void addFashionRule(StallPassengerFashionRule rule) {
        fashionRules.put(rule.getId(), rule);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addPurchaseRule(StallPassengerPurchaseRule rule) {
        purchaseRules.put(rule.getId(), rule);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StallPassengerFashionRule getFashionRules(String key) throws AlertException {
        if(!fashionRules.containsKey(key)){
            exceptionFactory.throwAlertException("passenger.rule.invalid");
        }
        return fashionRules.get(key);
    }

    @Override
    public StallPassengerPurchaseRule getPurchaseRules(String key) throws AlertException {
        if(!purchaseRules.containsKey(key)){
            exceptionFactory.throwAlertException("passenger.rule.invalid");
        }
        return purchaseRules.get(key);
    }

    @Override
    public PlayerStallPassenger getPlayerStallPassenger(PlayerStallPassengerInfo info,int id) throws AlertException {
        if(id>=info.getPassengers().length){
            exceptionFactory.throwAlertException("passenger.invalid");
        }else if(id<0){
            exceptionFactory.throwAlertException("passenger.invalid");
        }
        PlayerStallPassenger passenger =info.getPassengers()[id];
        if(null==passenger)
            exceptionFactory.throwAlertException("passenger.invalid");
        return passenger;
    }

    @Override
    public boolean check(PlayerStallPassengerInfo info, FarmPlayer fp) {
        PlayerStallPassenger[] passengers = info.getPassengers();
        boolean needUpdate = false;

        //判断时间刷新
        if(info.getRefreshDate().getTime()+StallConstant.PASSENGER_REFRESH<System.currentTimeMillis()){
            for(int i=0;i<passengers.length;i++){
                if(passengers[i] == null){//不满人
                    passengers[i] = generate(info,fp);
                    needUpdate = true;
                    break; //一次刷新一个路人
                }
            }
        }
        return needUpdate;
    }

    private PlayerStallPassenger generate(PlayerStallPassengerInfo info, FarmPlayer fp){
        StallPassengerFashionRule fRule = generateFashion();
        StallPassengerPurchaseRule pRule = generatePurchase(fp.getLevel());
        if(null!=fRule&&null!=pRule){
            PlayerStallPassenger passenger = new PlayerStallPassenger();
            passenger.setAction(StallConstant.PASSENGER_WALK);
            passenger.setFashionId(fRule.getId());
            passenger.setPurchaseId(pRule.getId());
            //赠积分
            int credit = 0;
            if(info.getCredit()< StallConstant.PASSENGER_CREDIT){
                if((int)(Math.random()*100)<pRule.getProbability()){
                    passenger.setCredit(pRule.getCredit());
                    credit = pRule.getCredit();
                }
            }
            Calendar ca = Calendar.getInstance();
            ca.setTime(info.getCreditDate());
            if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) ==  ca.get(Calendar.DAY_OF_YEAR)){
                info.setCredit(info.getCredit()+credit); //每日积分累计   .
            } else{
                info.setCredit(credit); //清空每日积分
            }

            info.setRefreshDate(new Date());
            return passenger;
        }
        return null;
    }

    private StallPassengerFashionRule generateFashion(){
        List<StallPassengerFashionRule> list = new ArrayList<StallPassengerFashionRule>(fashionRules.size());
        if(null==fashionRules)
            return null;
        for(Map.Entry<String, StallPassengerFashionRule> entry : fashionRules.entrySet() ){
            list.add(entry.getValue());
        }
        if(list.size()>0)
            return list.get((int)(Math.random()*list.size()));
        else
            return null;
    }
    private StallPassengerPurchaseRule generatePurchase(int level){
        List<StallPassengerPurchaseRule> list = new ArrayList<StallPassengerPurchaseRule>(purchaseRules.size());
        if(null==purchaseRules)
            return null;
        for(Map.Entry<String, StallPassengerPurchaseRule> entry : purchaseRules.entrySet() ){
            StallPassengerPurchaseRule rule = entry.getValue();
            if(rule.getLevel()>level)
                continue;
            list.add(entry.getValue());
        }
        if(list.size()>0)
            return list.get((int)(Math.random()*list.size()));
        else
            return null;
    }
}

