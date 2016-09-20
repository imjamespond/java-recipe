package com.pengpeng.stargame.room.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.room.RoomBuilder;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午3:30
 */
@Component
@DaoAnnotation(prefix = "room.packege.")
public class RoomPackegeDaoImpl extends RedisDao<RoomPackege> implements IRoomPackegeDao {

    @Autowired
    private RoomBuilder roomBuilder;


    @Override
    public Class<RoomPackege> getClassType() {
        return RoomPackege.class;
    }

    @Override
    public RoomPackege getRoomPackege(String pId) {
        RoomPackege roomPackege=this.getBean(pId);
        if(roomPackege==null){
            roomPackege= roomBuilder.newRoompackege(pId);
            this.saveBean(roomPackege);
        }
        return roomPackege;
    }
}
