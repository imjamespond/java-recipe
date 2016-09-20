package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IFriendRuleContainer;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-8-7
 * Time: 上午11:47
 */
@Component
public class FriendRuleContainerImpl implements IFriendRuleContainer {
    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Override
    public void addFriend(Friend my, Friend friend,String fid) {

        if(my.getId().equals(fid)) {
            return;
        }
        Player fplayer=playerDao.getBean(fid);
        if(fplayer.isNpc()){ //Npc帐号
            my.addFriend(fid);
        }   else{
            my.addFriend(fid);
            friend.addFriend(my.getId());
        }
    }

    @Override
    public void checkFriend(Friend my, Player mplayer,Player fplayer) throws AlertException {
        //1.检查是否好友身份,非好友身份不能进入
        if(mplayer.isNpc()){//NPc 帐号 直接通过
            return;
        }
        if(fplayer.isNpc()){//NPc 帐号 直接通过
            return;
        }
        if (!my.isFriend(fplayer.getId())){
            exceptionFactory.throwAlertException("room.friend.notenter");
        }
    }

    @Override
    public boolean hasNpcFriend(Friend my) {
        return false;
//        return my.isFriend(Constant.NPC_PLAYER_ID);
    }
}
