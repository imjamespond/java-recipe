package com.pengpeng.stargame.room.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.room.RoomBuilder;
import com.pengpeng.stargame.room.dao.IRoomEvaluateDao;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:45
 */
@Component
@DaoAnnotation(prefix = "room.evaluate.")
public class RoomEvaluateDaoImpl extends RedisDao<RoomEvaluate>  implements IRoomEvaluateDao {

    @Autowired
    private RoomBuilder roomBuilder;
    @Override
    public Class<RoomEvaluate> getClassType() {
        return RoomEvaluate.class;
    }

    @Override
    public RoomEvaluate getRoomEvaluate(String pid) {
        RoomEvaluate roomEvaluate=this.getBean(pid);
            if (roomEvaluate==null){
                roomEvaluate= roomBuilder.newFarmEvaluate(pid);
                this.saveBean(roomEvaluate);
            }
        if(roomEvaluate.check(System.currentTimeMillis())){
            roomEvaluate.setNextTime(DateUtil.getNextCountTime());
            this.saveBean(roomEvaluate);
        }

        return roomEvaluate;
    }
}
