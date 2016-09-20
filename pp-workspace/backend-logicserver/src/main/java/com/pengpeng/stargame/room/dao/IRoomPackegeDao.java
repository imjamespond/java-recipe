package com.pengpeng.stargame.room.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.room.RoomPackege;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午3:12
 */
public interface IRoomPackegeDao extends BaseDao<String,RoomPackege>{
   public RoomPackege getRoomPackege(String pId);
}
