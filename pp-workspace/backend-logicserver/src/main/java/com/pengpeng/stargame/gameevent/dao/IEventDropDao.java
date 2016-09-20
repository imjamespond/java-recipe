package com.pengpeng.stargame.gameevent.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.gameEvent.EventDrop;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午11:30
 */
public interface IEventDropDao  extends BaseDao<String,EventDrop> {
    EventDrop getEventDrop(String sceneId);
}
