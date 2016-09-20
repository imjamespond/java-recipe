package com.pengpeng.stargame.room.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.PlayerChat;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.pengpeng.stargame.room.container.IRoomExpansionRuleContainer;
import com.pengpeng.stargame.room.rule.RoomExpansionRule;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-22
 * Time: 下午3:57
 */
@Component
public class RoomExpansionRuleContainerImpl extends HashMapContainer<Integer,RoomExpansionRule> implements IRoomExpansionRuleContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public void extensionFinish(RoomPlayer roomPlayer,Player player) throws AlertException {
        //是否在扩建
        //判断钱够不够
        //扩建
        if(roomPlayer.getExpansionId()==0){
            exceptionFactory.throwAlertException("没有进行扩建");
        }
        RoomExpansionRule roomExpansionRule=getElement(roomPlayer.getExpansionId());
        int gold=extensionNeedGold(roomPlayer);
        if(player.getGoldCoin()<gold){
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }
//        player.decGoldCoin(gold);
        playerRuleContainer.decGoldCoin(player,gold, PlayerConstant.GOLD_ACTION_13);


        roomPlayer.setcExpansionId(roomPlayer.getExpansionId());//设置已经扩建到了 哪个Id
        roomPlayer.setExpansionId(0);//把正在扩建的 Id 设置为 0
        if(roomExpansionRule.getLocation()==0){
            roomPlayer.setX(roomPlayer.getX()+1);
        }
        if(roomExpansionRule.getLocation()==1){
            roomPlayer.setY(roomPlayer.getY() + 1);
        }
    }

    @Override
    public int extensionNeedGold(RoomPlayer roomPlayer) {
        RoomExpansionRule roomExpansionRule=getElement(roomPlayer.getExpansionId());
        Date now=new Date();
        double sTime=(roomPlayer.getExpansionEndTime().getTime() -now.getTime())/(3600l*1000l);
        int gold= (int) (sTime*roomExpansionRule.getParameter());
        return gold;
    }

    @Override
    public void extensionStart(RoomPlayer roomPlayer, Player player) throws AlertException {
        //农场等级判断
        //游戏币的判断
        //扩建
        if(roomPlayer.getExpansionId()!=0){
            exceptionFactory.throwAlertException("正在扩建中，不能进行扩建！");
        }
        RoomExpansionRule roomExpansionRule=getElement(roomPlayer.getcExpansionId()+1);
        if(roomExpansionRule==null){
            exceptionFactory.throwAlertException("扩建达到了最大！");
        }
        if(player.getGameCoin()<roomExpansionRule.getGameCoin()){
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }

//        player.decGameCoin(roomExpansionRule.getGameCoin());
        playerRuleContainer.decGameCoin(player,roomExpansionRule.getGameCoin());


        roomPlayer.setExpansionId(roomPlayer.getcExpansionId()+1);
        Date now=new Date();
        roomPlayer.setExpansionEndTime(DateUtil.addSecond(now,roomExpansionRule.getTime()));
//        roomPlayer.setExpansionEndTime(DateUtil.addSecond(now,10));
    }
}
