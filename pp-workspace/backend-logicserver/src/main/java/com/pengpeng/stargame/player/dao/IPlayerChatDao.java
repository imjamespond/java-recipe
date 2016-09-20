package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.player.PlayerChat;

/**
 * User: mql
 * Date: 13-12-2
 * Time: 下午4:09
 */
public interface IPlayerChatDao extends BaseDao<String,PlayerChat> {

    public PlayerChat getPlayerChat(String pid);
}
