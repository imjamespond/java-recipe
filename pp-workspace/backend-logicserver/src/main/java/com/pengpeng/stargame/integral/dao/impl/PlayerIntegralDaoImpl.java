package com.pengpeng.stargame.integral.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.integral.dao.IPlayerIntegralDao;
import com.pengpeng.stargame.model.integral.IntegralAction;
import com.pengpeng.stargame.model.integral.PlayerIntegralShow;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:57
 */
@Component
@DaoAnnotation(prefix = "player.Integral.show")
public class PlayerIntegralDaoImpl extends RedisDao<PlayerIntegralShow> implements IPlayerIntegralDao {
    @Override
    public Class<PlayerIntegralShow> getClassType() {
        return PlayerIntegralShow.class;
    }

    @Override
    public PlayerIntegralShow getPlayerIntegralShow(String pid) {
        PlayerIntegralShow playerIntegralShow=getBean(pid);
        if(playerIntegralShow==null){
            playerIntegralShow=new PlayerIntegralShow();
            playerIntegralShow.setPid(pid);
            playerIntegralShow.setIntegralActionList(new ArrayList<IntegralAction>());
            playerIntegralShow.setNextTime(DateUtil.getNextCountTime());
            saveBean(playerIntegralShow);
            return playerIntegralShow;
        }
        Date now=new Date();{
            if(now.after(playerIntegralShow.getNextTime())){
                playerIntegralShow.setIntegralActionList(new ArrayList<IntegralAction>());
                playerIntegralShow.setNextTime(DateUtil.getNextCountTime());
                saveBean(playerIntegralShow);
            }
        }
        return playerIntegralShow;
    }
}
