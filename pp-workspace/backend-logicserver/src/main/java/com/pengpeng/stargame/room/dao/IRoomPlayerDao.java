package com.pengpeng.stargame.room.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.room.RoomPlayer;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午2:59
 */
public interface IRoomPlayerDao  extends BaseDao<String,RoomPlayer>{

    public RoomPlayer getRoomPlayer(String pId);
}
