package com.pengpeng.stargame.stall.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.stall.*;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.stall.dao.IPlayerStallPassengerDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.stall.passenger.")
public class PlayerStallPassengerDaoImpl extends RedisDao<PlayerStallPassengerInfo> implements IPlayerStallPassengerDao {

    @Override
    public Class<PlayerStallPassengerInfo> getClassType() {
        return PlayerStallPassengerInfo.class;
    }

    @Override
    public PlayerStallPassengerInfo getPlayerStallPassenger(String pid) {
        PlayerStallPassengerInfo playerStall=this.getBean(pid);
        if(null == playerStall){
            playerStall = new PlayerStallPassengerInfo();
            playerStall.setPid(pid);

            saveBean(playerStall);
        }

        if(null==playerStall.getRefreshDate()){
            playerStall.setRefreshDate(new Date(0));
        }
        if(null==playerStall.getCreditDate()){
            playerStall.setCreditDate(new Date(0));
        }

        if(null == playerStall.getPassengers()){
            playerStall.setPassengers(new PlayerStallPassenger[StallConstant.PASSENGER_NUM]);
        }

        return playerStall;
    }

}
