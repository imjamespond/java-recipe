package com.pengpeng.stargame.vip.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.dao.IVipDao;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午4:43
 */
@Component
@DaoAnnotation(prefix = "vip.")
public class VipDaoImpl extends RedisDao<PlayerVip> implements IVipDao  {
    @Override
    public Class<PlayerVip> getClassType() {
        return PlayerVip.class;
    }

    @Override
    public PlayerVip getPlayerVip(String pid) {
        PlayerVip playerVip=getBean(pid);
        if(playerVip==null){
            playerVip=new PlayerVip();
            playerVip.setPid(pid);
            playerVip.setViP(0);
            playerVip.setLevel(1);
            saveBean(playerVip);
            return playerVip;
        }
        Date now=new Date();
        if(playerVip.getViP()==1){

            if(playerVip.getEndTime()!=null&&playerVip.getEndTime().getTime()<now.getTime()){
                playerVip.setViP(0);
                playerVip.setEndTime(null);
                saveBean(playerVip);
            }
        }
        return playerVip;
    }

    private Date getEndTime(PlayerVip playerVip){
        Date now=new Date();
        int sends=0;
        if(playerVip.getStartTime()!=null&&playerVip.getHours()!=0){
            Date gameEndTime= DateUtil.addMinute(playerVip.getStartTime(),playerVip.getHours()*60);
            if(gameEndTime.getTime()>now.getTime()){
                sends= (int) ((gameEndTime.getTime()-now.getTime())/1000);
            }
        }
        if(playerVip.getEndTime()!=null){
            return  DateUtil.addSecond(playerVip.getEndTime(),playerVip.getHours()*60);
        }
        return  DateUtil.addSecond(now,playerVip.getHours()*60);
    }
}
