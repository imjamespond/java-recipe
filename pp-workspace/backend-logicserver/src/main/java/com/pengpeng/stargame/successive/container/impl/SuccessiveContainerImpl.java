package com.pengpeng.stargame.successive.container.impl;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.successive.container.ISuccessiveContainer;
import com.pengpeng.stargame.successive.rule.SuccessiveRule;
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
public class SuccessiveContainerImpl extends HashMapContainer<String, SuccessiveRule> implements ISuccessiveContainer {

    private static final int MAXDAY = 7;



    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    public void detect(PlayerSuccessive ps) {
        //ps.setGetPrize(true);

        //当日
        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_YEAR);
        int year = today.get(Calendar.YEAR);

        //上次登陆时间
        Calendar lastLogin = Calendar.getInstance();
        lastLogin.setTime(ps.getLastLogin());
        int lastLoginDay = lastLogin.get(Calendar.DAY_OF_YEAR);
        int lastLoginYear = lastLogin.get(Calendar.YEAR);

        //连续登陆
        boolean reset = true;
        if(year == lastLoginYear){
            if(day == lastLoginDay+1){
                ps.incDay(1);
                reset = false;
            }else if(day == lastLoginDay){  //同日登陆
                reset = false;
            }
        }
        else if(year == lastLoginYear+1){
            if(day == 1 && (lastLoginDay == 365 || lastLoginDay == 366)){
                ps.incDay(1);
                reset = false;
            }
        }
        //重置
        if(reset) {
            ps.setDay(1);
            ps.setGetPrize(0);
        }
        //System.out.println(day +" "+lastLoginDay);
        if (ps.getDay() > MAXDAY) {
            ps.setDay(1);
            ps.setGetPrize(0);
        }

        ps.setLastLogin(new Date());
    }

    public void getPrize(String day, PlayerSuccessive ps, Player player, FarmPackage farmPackage, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException {

        int isGetPrize = 1 << Integer.valueOf(day);

        if ((ps.getGetPrize() & isGetPrize) > 0) {
            //已领取
            exceptionFactory.throwAlertException("already.get.prize");
        }

        SuccessiveRule rule = getElement(day);
        if (null != rule) {
            if (null != rule.getItemNumList()) {
                if (baseItemRulecontainer.checkFarmPackge(ps.getPid(), rule.getItemNumList())) {
                    exceptionFactory.throwAlertException("successive.farm.full");
                }
                if (baseItemRulecontainer.checkFashionPackge(ps.getPid(), rule.getItemNumList())) {
                    exceptionFactory.throwAlertException("successive.fashion.full");
                }

                for (ItemData itemNum : rule.getItemNumList()) {
                    baseItemRulecontainer.addGoodsNoSave(itemNum.getItemId(), itemNum.getNum(), farmPackage, roomPackege, fashionCupboard,farmDecoratePkg);
                }
            }
            player.incGameCoin(rule.getGameCoin());
            //player.incGoldCoin(rule.getGoldCoin());
            playerRuleContainer.incGoldCoin(player,rule.getGoldCoin(), PlayerConstant.GOLD_ACTION_30);

            ps.setGetPrize(ps.getGetPrize() | isGetPrize);
        }
    }


    public int getNum(int day,int getPrize){
        int num = 0;
        for(int i=1; i<=day;i++){
            int isGetPrize = 1 << i;
            if ((getPrize & isGetPrize) == 0) {
                num++;
            }
        }
        return num;
    }



    /*
    public static void main(String[] args){
        PlayerSuccessive ps = new PlayerSuccessive();
        Calendar today = Calendar.getInstance();
        today.set(2013,Calendar.NOVEMBER,4,8,0,0);//+8timezone
        ps.setLastLogin(today.getTime());
        SuccessiveContainerImpl test = new SuccessiveContainerImpl();
        test.detect(ps);
    }*/
}


