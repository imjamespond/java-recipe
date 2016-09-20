package com.pengpeng.stargame.gameevent.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.PlayerEvent;

/**
 * User: mql
 * Date: 13-12-23
 * Time: 上午10:47
 */
public interface IPlayerEventDao extends BaseDao<String,PlayerEvent> {

    PlayerEvent getPlayerEvent(String  pid);
}
