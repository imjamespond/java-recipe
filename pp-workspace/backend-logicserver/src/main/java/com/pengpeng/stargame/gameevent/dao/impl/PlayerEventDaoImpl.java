package com.pengpeng.stargame.gameevent.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.gameevent.dao.IPlayerEventDao;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.PlayerEvent;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * User: mql
 * Date: 13-12-23
 * Time: 上午10:47
 */
@Component
@DaoAnnotation(prefix = "gameevent.playerevent.")
public class PlayerEventDaoImpl extends RedisDao<PlayerEvent> implements IPlayerEventDao{
    @Override
    public Class<PlayerEvent> getClassType() {
        return PlayerEvent.class;
    }

    @Override
    public PlayerEvent getPlayerEvent(String pid) {
        PlayerEvent playerEvent=getBean(pid);
        if(playerEvent==null){
            playerEvent=new PlayerEvent(pid);
            saveBean(playerEvent);
            return playerEvent;
        }
        Date now=new Date();
        if(playerEvent.getNextTime()==null||now.getTime()>playerEvent.getNextTime().getTime()){
            playerEvent.setDropInfo(new HashMap<String, Integer>());
            playerEvent.setNextTime(DateUtil.getNextCountTime());
            saveBean(playerEvent);
        }
        return playerEvent;
    }
}
