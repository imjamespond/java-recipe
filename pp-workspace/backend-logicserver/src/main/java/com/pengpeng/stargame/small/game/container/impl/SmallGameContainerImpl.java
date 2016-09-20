package com.pengpeng.stargame.small.game.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.small.game.container.ISmallGameContainer;
import com.pengpeng.stargame.small.game.rule.SmallGameRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class SmallGameContainerImpl extends HashMapContainer<String, SmallGameRule> implements ISmallGameContainer {
    private static final int FREETIME = 3;
    private static final long HOURS12 = 12L*3600L*1000L;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;


    @Override
    public void checkFree(PlayerSmallGame ps, int type) throws AlertException {
        //是否是当日
        Calendar ca = Calendar.getInstance();
        ca.setTime(ps.getLastLoginTime());
        if(ca.get(Calendar.DAY_OF_YEAR) != Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            ps.getFreeTime().put(type, FREETIME);
        }
    }

    @Override
    public void buy(PlayerSmallGame ps, Player p, int type,int gold) throws AlertException{
        SmallGameRule rule = this.getElement(String.valueOf(type));
        if(rule.getPrices().containsKey(gold)){
            int time = rule.getPrices().get(gold);
            if(p.getGameCoin()<=gold){
                exceptionFactory.throwAlertException("game.coin.notenough");
            }
            //p.decGoldCoin(gold);
            playerRuleContainer.decGameCoin(p,gold);   //改为游戏币了
            //playerRuleContainer.decGoldCoin(p,gold, PlayerConstant.GOLD_ACTION_45);
            int goldTime = 0;
            if(ps.getGoldTime().containsKey(type)){
                goldTime = ps.getGoldTime().get(type);
            }
            ps.getGoldTime().put(type,goldTime+time);//原来次数加购买次数
        }else {
            exceptionFactory.throwAlertException("price.not.exist");
        }
    }

    @Override
    public void update(PlayerSmallGame ps, int type, int score,long defer) throws AlertException{
        SmallGameRule rule = this.getElement(String.valueOf(type));
        //超时验证
        if(rule.getTimeLimit()>0) {
        long diff = System.currentTimeMillis()-ps.getVerifyTime()-defer;
        if(diff>rule.getTimeLimit()){
            exceptionFactory.throwAlertException("small.game.time.out");
        }
        }


        int week = -1;
        if(ps.getScoreWeek().containsKey(type)){
            week = ps.getScoreWeek().get(type);
        }
        int thisWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        if(thisWeek!=week){
            ps.getScore().put(type, score);
            ps.getScoreWeek().put(type, thisWeek);
            return;
        }
        int preScore = 0;
        if(ps.getScore().containsKey(type)){
            preScore = ps.getScore().get(type);
        }
        if(preScore<score){
            ps.getScore().put(type, score);
            return;
        }
    }

    @Override
    public void deduct(PlayerSmallGame ps, int type) throws AlertException{
        int freeTime = 0;
        if(ps.getFreeTime().containsKey(type)){
            freeTime = ps.getFreeTime().get(type);
        }
        if(freeTime<=0){
            int goldTime = 0;
            if(ps.getGoldTime().containsKey(type)){
                goldTime = ps.getGoldTime().get(type);
            }
            if(goldTime<=0){
                exceptionFactory.throwAlertException("small.game.out.of.time");
            }else{ //收费
                ps.getGoldTime().put(type, goldTime - 1);
            }
        }else{//免费
            ps.getFreeTime().put(type,freeTime-1);
        }

        ps.setVerifyTime(System.currentTimeMillis()); //防作弊时间
    }

    @Override
    public String getWeekKey(int type) {
        Calendar ca = Calendar.getInstance();
        int week = ca.get(Calendar.WEEK_OF_YEAR);
        int year = ca.get(Calendar.YEAR);
        String weekKey = String.valueOf(type)+"."+String.valueOf(year)+String.valueOf(week);
        return weekKey;
    }

    @Override
    public String getDayKey(int type) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(System.currentTimeMillis()+HOURS12);
        int day = ca.get(Calendar.DAY_OF_YEAR);
        String dayKey = String.valueOf(type)+"."+String.valueOf(day);
        return dayKey;
    }

    @Override
    public String getMax(int type) {
        return String.valueOf(type)+"Max";
    }

}


