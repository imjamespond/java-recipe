package com.pengpeng.stargame.lucky.tree.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.lucky.tree.container.ILuckyTreeContainer;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeDataRule;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeRule;
import com.pengpeng.stargame.model.lucky.tree.LuckyTreeCall;
import com.pengpeng.stargame.model.lucky.tree.LuckyTreeConstant;
import com.pengpeng.stargame.model.lucky.tree.PlayerLuckyTree;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class LuckyTreeContainerImpl implements ILuckyTreeContainer {

    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    Map<Integer,LuckyTreeRule> luckyTreeRuleMap = new HashMap<Integer,LuckyTreeRule>();//level->rule

    private LuckyTreeRuleVO[] luckyTreeRuleVOs;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;

    @Override
    public void addLuckyTreeRuleMap(LuckyTreeRule rule) {
        luckyTreeRuleMap.put(rule.getLevel(),rule );
    }

    @Override
    public Map<Integer,LuckyTreeRule> getLuckyTreeRuleMap() {
       return luckyTreeRuleMap;
    }

    @Override
    public int getGoldCallNum(PlayerLuckyTree playerLuckyTree,LuckyTreeRule rule) {
        return rule.getGoldCallNum() - playerLuckyTree.getGoldNum() - LuckyTreeConstant.GOLD_CALL_EXTRA + playerLuckyTree.getGoldExtra();
    }
    @Override
    public LuckyTreeRuleVO[] getLuckyTreeRuleVOs() {
        return luckyTreeRuleVOs;
    }
    @Override
    public void setLuckyTreeRuleVOs(LuckyTreeRuleVO[] luckyTreeRuleVOs) {
        this.luckyTreeRuleVOs = luckyTreeRuleVOs;
    }

    private void waterCheck(PlayerLuckyTree playerLuckyTree,LuckyTreeRule rule) throws AlertException{
        Date now = new Date();
        if(playerLuckyTree.getWaterDate().getTime()+rule.getWaterCd()>now.getTime()){
            exceptionFactory.throwAlertException("water.cool.down");
        }
        if(DateUtil.getDayOfYear(playerLuckyTree.getWaterDate())==DateUtil.getDayOfYear(now)){
            if(playerLuckyTree.getWaterNum()>=rule.getWaterNum()){
                exceptionFactory.throwAlertException("water.over.limit");
            }
            playerLuckyTree.setWaterNum(playerLuckyTree.getWaterNum()+1);
        }else{
            playerLuckyTree.setWaterNum(1);
        }
        playerLuckyTree.setWaterDate(now);
    }

    private void waterFriCheck(PlayerLuckyTree playerLuckyTree,LuckyTreeRule rule) throws AlertException{
        Date now = new Date();
        if(playerLuckyTree.getWaterDate().getTime()+rule.getWaterCd()>now.getTime()){
            exceptionFactory.throwAlertException("water.cool.down");
        }
        if(DateUtil.getDayOfYear(playerLuckyTree.getWaterDate())==DateUtil.getDayOfYear(now)){
            if(playerLuckyTree.getWaterFri()>=rule.getWaterFri()){
                exceptionFactory.throwAlertException("water.over.limit");
            }
            playerLuckyTree.setWaterFri(playerLuckyTree.getWaterFri() + 1);
        }else{
            playerLuckyTree.setWaterFri(1);
        }
        playerLuckyTree.setWaterDate(now);
    }

    @Override
    public void water(PlayerLuckyTree playerLuckyTree) throws AlertException {
        int level = playerLuckyTree.getLevel();
        //经验一次增1
        if(luckyTreeRuleMap.containsKey(level)){
            LuckyTreeRule rule = luckyTreeRuleMap.get(level);
            if(rule.getNeedExp() == 0){
                exceptionFactory.throwAlertException("lucky.tree.full.level");
            }
            waterCheck(playerLuckyTree,rule);//检测
            playerLuckyTree.setExp(playerLuckyTree.getExp() + rule.getWaterExp());//增加经验
            if(playerLuckyTree.getExp()>=rule.getNeedExp()){
                playerLuckyTree.setLevel(level+1);//upgrade
            }
        }else {
            exceptionFactory.throwAlertException("lucky.tree.rule.invalid");//没有数据
        }
    }

    @Override
    public void waterFri(PlayerLuckyTree playerLuckyTree) throws AlertException {
        int level = playerLuckyTree.getLevel();
        //经验一次增1
        if(luckyTreeRuleMap.containsKey(level)){
            LuckyTreeRule rule = luckyTreeRuleMap.get(level);
            if(rule.getNeedExp() == 0){
                exceptionFactory.throwAlertException("lucky.tree.full.level");
            }
            waterFriCheck(playerLuckyTree, rule);//好友的检测
            playerLuckyTree.setExp(playerLuckyTree.getExp() + rule.getWaterExp());//增加经验
            if(playerLuckyTree.getExp()>=rule.getNeedExp()){
                playerLuckyTree.setLevel(level+1);//upgrade
            }
        }else {
            exceptionFactory.throwAlertException("lucky.tree.rule.invalid");//没有数据
        }
    }


    private boolean freeCall(PlayerLuckyTree playerLuckyTree,LuckyTreeRule rule) throws AlertException{
        Date now = new Date();
        //免费招财
        if(DateUtil.getDayOfYear(playerLuckyTree.getFreeDate())==DateUtil.getDayOfYear(now)){
            if(playerLuckyTree.getFreeNum()<rule.getFreeCallNum()){
                playerLuckyTree.setFreeNum(playerLuckyTree.getFreeNum()+1);
            } else{
                return false;
            }
        }else{
            playerLuckyTree.setFreeNum(1);
        }
        playerLuckyTree.setFreeDate(now);

        return true;
    }

    private void goldCall(PlayerLuckyTree playerLuckyTree,LuckyTreeRule rule) throws AlertException{
        Date now = new Date();
        //达人币招财
        if(DateUtil.getDayOfYear(playerLuckyTree.getGoldDate())==DateUtil.getDayOfYear(now)){
            if(getGoldCallNum(playerLuckyTree,rule)<=0){
                exceptionFactory.throwAlertException("gold.call.limit");
            }
            playerLuckyTree.setGoldNum(playerLuckyTree.getGoldNum()+1);
        }else{
            playerLuckyTree.setGoldNum(1);
        }
        playerLuckyTree.setGoldDate(now);
    }

    @Override
    public void add(Player player, PlayerLuckyTree playerLuckyTree) throws AlertException {
        Date now = new Date();
        LuckyTreeRule rule = luckyTreeRuleMap.get(playerLuckyTree.getLevel());
        //达人币招财
        if(DateUtil.getDayOfYear(playerLuckyTree.getGoldExtraDate())==DateUtil.getDayOfYear(now)){
            if(playerLuckyTree.getGoldExtra()>=LuckyTreeConstant.GOLD_CALL_EXTRA){
                exceptionFactory.throwAlertException("gold.call.add.limit");
            }
            playerLuckyTree.setGoldExtra(playerLuckyTree.getGoldExtra()+1);
        }else{
            playerLuckyTree.setGoldExtra(1);
        }

        //达人币
        if(player.getGoldCoin()< LuckyTreeConstant.GOLD_CALL_COST){
            exceptionFactory.throwAlertException("goldcoin.notenough");//达人币不足
        }
        //player.decGoldCoin(LuckyTreeConstant.GOLD_CALL_COST);
        playerRuleContainer.decGoldCoin(player,LuckyTreeConstant.GOLD_CALL_COST, PlayerConstant.GOLD_ACTION_20);
        playerLuckyTree.setGoldExtraDate(now);
    }

    @Override
    public LuckyTreeCall call(Player player, PlayerLuckyTree playerLuckyTree) throws AlertException {
        int level = playerLuckyTree.getLevel();
        if(luckyTreeRuleMap.containsKey(level)){
            LuckyTreeRule rule = luckyTreeRuleMap.get(level);
            LuckyTreeCall luckyTreeCall = new LuckyTreeCall();
            int critical = 1;
            Random random = new Random();
            int randomInt = random.nextInt(1000);
            if(freeCall(playerLuckyTree,rule)){ //如果有免费的
                if(randomInt<rule.getFreeCritical()){
                    critical = rule.getFreeMultiple();
                }
                luckyTreeCall.setGameCoin(rule.getFreeGameCoin());
            }else{
                goldCall(playerLuckyTree,rule);//用达人币招财
                if(randomInt<rule.getFreeCritical()){
                    critical = rule.getFreeMultiple();
                }
                if(!rule.getDataMap().containsKey(playerLuckyTree.getGoldNum())) {
                    exceptionFactory.throwAlertException("lucky.tree.rule.invalid");//没有数据
                }

                LuckyTreeDataRule dataRule = rule.getDataMap().get(playerLuckyTree.getGoldNum());
                luckyTreeCall.setCredit(dataRule.credit);
                //达人币
                if(player.getGoldCoin()<dataRule.gold){
                    exceptionFactory.throwAlertException("goldcoin.notenough");//达人币不足
                }
                //player.decGoldCoin(dataRule.gold);
                playerRuleContainer.decGoldCoin(player,dataRule.gold, PlayerConstant.GOLD_ACTION_21);
                luckyTreeCall.setGameCoin(rule.getGoldGameCoin());
                /**
                 * 成就统计 累积招财次数
                 */
                successRuleContainer.updateSuccessNum(player.getId(),14,1,"");
                /**
                 * 任务的
                 */
                taskRuleContainer.updateTaskNum(player.getId(), TaskConstants.CONDI_TYPE_19,"",1);

            }
            luckyTreeCall.setCritical(critical);
            return luckyTreeCall;
        } else {
            exceptionFactory.throwAlertException("lucky.tree.rule.invalid");//没有数据
        }
        return null;
    }

}



