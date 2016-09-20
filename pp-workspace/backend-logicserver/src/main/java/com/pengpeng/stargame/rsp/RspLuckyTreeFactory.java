package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.lucky.tree.container.ILuckyTreeContainer;
import com.pengpeng.stargame.lucky.tree.dao.IPlayerLuckyTreeDao;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeDataRule;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeRule;
import com.pengpeng.stargame.model.lucky.tree.LuckyTreeConstant;
import com.pengpeng.stargame.model.lucky.tree.PlayerLuckyTree;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO;
import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessivePrizeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */
@Component
public class RspLuckyTreeFactory extends RspFactory {

    @Autowired
    private ILuckyTreeContainer luckyTreeContainer;
    @Autowired
    private IPlayerLuckyTreeDao playerLuckyTreeDao;

    public LuckyTreeVO LuckyTreeVO(PlayerLuckyTree playerLuckyTree){
        Date now = new Date();
        boolean needSave = false;
        LuckyTreeVO vo = new LuckyTreeVO();
        if(!luckyTreeContainer.getLuckyTreeRuleMap().containsKey(playerLuckyTree.getLevel())){
            return null;
        }
        LuckyTreeRule rule = luckyTreeContainer.getLuckyTreeRuleMap().get(playerLuckyTree.getLevel());


        if(DateUtil.getDayOfYear(playerLuckyTree.getWaterDate())!=DateUtil.getDayOfYear(now)){
            playerLuckyTree.setWaterNum(0);
            playerLuckyTree.setWaterFri(0);
            needSave = true;
        }
        if(DateUtil.getDayOfYear(playerLuckyTree.getFreeDate())!=DateUtil.getDayOfYear(now)){
            playerLuckyTree.setFreeNum(0);
            needSave = true;
        }
        if(DateUtil.getDayOfYear(playerLuckyTree.getGoldDate())!=DateUtil.getDayOfYear(now)){
            playerLuckyTree.setGoldNum(0);
            needSave = true;
        }
        if(DateUtil.getDayOfYear(playerLuckyTree.getGoldExtraDate())!=DateUtil.getDayOfYear(now)){
            playerLuckyTree.setGoldExtra(0);
            needSave = true;
        }
        if(needSave){
            playerLuckyTreeDao.saveBean(playerLuckyTree);
        }

        vo.setLevel(playerLuckyTree.getLevel());
        vo.setExp(playerLuckyTree.getExp());
        vo.setWaterCd(rule.getWaterCd() - now.getTime() + playerLuckyTree.getWaterDate().getTime());
        vo.setNeedExp(rule.getNeedExp());

        vo.setFreeCallNum(rule.getFreeCallNum() - playerLuckyTree.getFreeNum());
        vo.setGoldCallNum(luckyTreeContainer.getGoldCallNum(playerLuckyTree,rule));
        vo.setWaterFri(rule.getWaterFri() - playerLuckyTree.getWaterFri());
        vo.setWaterNum(rule.getWaterNum() - playerLuckyTree.getWaterNum());
        LuckyTreeDataRule dataRule = rule.getDataMap().get(playerLuckyTree.getGoldNum()+1);
        if(dataRule != null)
        vo.setGoldCall(dataRule.gold);

        return vo;
    }



}
