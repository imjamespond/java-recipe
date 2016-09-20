package com.pengpeng.stargame.vip.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vip.dao.IVipDao;
import com.pengpeng.stargame.vip.rule.PayMemberRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-19
 * Time: 上午11:58
 */
@Component
public class PayMemberRuleContainerImpl extends HashMapContainer<String,PayMemberRule> implements IPayMemberRuleContainer {
  @Autowired
   private IPlayerDao playerDao;
    @Autowired
    private IVipDao vipDao;

    @Override
    public void addVipTime(String pid, int hour) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        if(playerVip.getViP()==1){  //如果是VIp 累加时间
            playerVip.setEndTime(DateUtil.addMinute(playerVip.getEndTime(), hour*60));
        }else{
            playerVip.setViP(1);
            playerVip.setEndTime(DateUtil.addMinute(new Date(),hour*60));
        }
        vipDao.saveBean(playerVip);
    }

    @Override
    public List<PayMemberRule> getListByLevel(int level) {
        List<PayMemberRule> payMemberRules=new ArrayList<PayMemberRule>();
        for(PayMemberRule payMemberRule:items.values()){
            if(payMemberRule.getLevel()==level){
                payMemberRules.add(payMemberRule);
            }
        }
        return payMemberRules;
    }

    @Override
    public PayMemberRule getPayMemberRuleByTypeAndLevel(int type, int level) {
        for(PayMemberRule payMemberRule:items.values()){
            if(payMemberRule.getLevel()==level&&payMemberRule.getType()==type){
                return payMemberRule;
            }
        }
        return null;
    }

    @Override
    public boolean isPayMember(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        if(playerVip.getViP()==1){
            return true;
        }
        return false;
    }

    @Override
    public int getOrderMaxNum(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        return (int) getPayMemberRuleByTypeAndLevel(3,playerVip.getLevel()).getData();
    }

    @Override
    public int getHelpMaxNum(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        return (int) getPayMemberRuleByTypeAndLevel(5,playerVip.getLevel()).getData();
    }

    @Override
    public int getSonorusMaxNum(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        return (int) getPayMemberRuleByTypeAndLevel(6,playerVip.getLevel()).getData();
    }

    @Override
    public int getSendGiftMaxNum(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        return (int) getPayMemberRuleByTypeAndLevel(7,playerVip.getLevel()).getData();
    }

    @Override
    public int getFamilyTaskAdd(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        return (int) getPayMemberRuleByTypeAndLevel(2,playerVip.getLevel()).getData();
    }

    @Override
    public int getFreeZanNum(String pid) {
        PlayerVip playerVip=vipDao.getPlayerVip(pid);
        return (int) getPayMemberRuleByTypeAndLevel(9,playerVip.getLevel()).getData();
    }


}
