package com.pengpeng.stargame.room.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.room.RoomBuilder;
import com.pengpeng.stargame.room.container.IRoomExpansionRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.room.rule.RoomExpansionRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午3:00
 */
@Component
@DaoAnnotation(prefix = "room.")
public class RoomPlayerDaoImpl extends RedisDao<RoomPlayer> implements IRoomPlayerDao  {

    @Autowired
    private RoomBuilder roomBuilder;
    @Autowired
    private IRoomExpansionRuleContainer roomExpansionRuleContainer;
    @Override
    public Class<RoomPlayer> getClassType() {
        return RoomPlayer.class;
    }

    @Override
    public RoomPlayer getRoomPlayer(String pId) {
        RoomPlayer roomPlayer=this.getBean(pId);
        if(roomPlayer==null){
            roomPlayer= roomBuilder.newRoomPlayer(pId);
            this.saveBean(roomPlayer);
        }
        /**
         * 扩建处理
         */
        if(roomPlayer.getExpansionId()!=0){
            RoomExpansionRule roomExpansionRule=roomExpansionRuleContainer.getElement(roomPlayer.getExpansionId());
            Date now=new Date();
            if(now.getTime()>roomPlayer.getExpansionEndTime().getTime()){
                roomPlayer.setcExpansionId(roomPlayer.getExpansionId());//设置已经扩建到了 哪个Id
                roomPlayer.setExpansionId(0);
                if(roomExpansionRule.getLocation()==0){
                   roomPlayer.setX(roomPlayer.getX()+1);
                }
                if(roomExpansionRule.getLocation()==1){
                    roomPlayer.setY(roomPlayer.getY()+1);
                }
               saveBean(roomPlayer);
            }
        }
        return roomPlayer;
    }
}
