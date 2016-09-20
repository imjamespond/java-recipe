package com.pengpeng.stargame.room.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.room.RoomEvaluate;

/**
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:44
 */
public interface IRoomEvaluateDao extends BaseDao<String,RoomEvaluate> {

    public RoomEvaluate getRoomEvaluate(String pid);
}
