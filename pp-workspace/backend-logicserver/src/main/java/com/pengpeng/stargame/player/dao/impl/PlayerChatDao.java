package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.player.PlayerChat;
import com.pengpeng.stargame.player.dao.IPlayerChatDao;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-12-2
 * Time: 下午4:10
 */
@Component
@DaoAnnotation(prefix="p.chat.")
public class PlayerChatDao extends RedisDao<PlayerChat> implements IPlayerChatDao {
    @Override
    public Class<PlayerChat> getClassType() {
        return PlayerChat.class;
    }

    @Override
    public PlayerChat getPlayerChat(String pid) {
        PlayerChat playerChat=getBean(pid);
        if(playerChat==null){
            playerChat=new PlayerChat();
            playerChat.setPid(pid);
            playerChat.setNum(0);
            playerChat.setNextTime(DateUtil.getNextCountTime());
            saveBean(playerChat);
            return playerChat;
        }
        Date now=new Date();
        if(playerChat.getNextTime().getTime()<=now.getTime()){
            playerChat.setNum(0);
            playerChat.setNextTime(DateUtil.getNextCountTime());
            saveBean(playerChat);
        }
        return playerChat;
    }
}
