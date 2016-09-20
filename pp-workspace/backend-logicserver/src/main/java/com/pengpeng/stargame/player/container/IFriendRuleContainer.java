package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.Player;

/**
 * User: mql
 * Date: 13-8-7
 * Time: 上午11:46
 */
public interface IFriendRuleContainer {
    /**
     * 加好友
     * @param my
     * @param friend
     * @param fid
     */

    public void  addFriend(Friend my,Friend friend,String fid);

    /**
     *
     * @param my
     * @param mplayer
     * @param fplayer
     * @throws AlertException
     */

    public void checkFriend(Friend my, Player mplayer,Player fplayer) throws AlertException;

    public boolean hasNpcFriend(Friend my);

}
