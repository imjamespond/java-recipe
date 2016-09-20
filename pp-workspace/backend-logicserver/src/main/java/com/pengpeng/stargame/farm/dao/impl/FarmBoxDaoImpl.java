package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.dao.IFarmBoxDao;
import com.pengpeng.stargame.model.farm.box.PlayerFarmBox;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午3:01
 */
@Component
@DaoAnnotation(prefix = "farm.box.")
public class FarmBoxDaoImpl extends RedisDao<PlayerFarmBox> implements IFarmBoxDao {
    @Override
    public Class<PlayerFarmBox> getClassType() {
        return PlayerFarmBox.class;
    }

    @Override
    public PlayerFarmBox getPlayerFarmBox(String pid) {
        PlayerFarmBox playerFarmBox=getBean(pid);
        if(playerFarmBox==null){
            playerFarmBox=new PlayerFarmBox();
            playerFarmBox.setPid(pid);
            playerFarmBox.setRefreshDate(DateUtil.getNextCountTime());
            saveBean(playerFarmBox);
            return playerFarmBox;
        }
        Date now=new Date();
        if(playerFarmBox.getRefreshDate().before(now)){
            playerFarmBox.setBoxstatu(0);
            playerFarmBox.setAllboxnum(0);
            playerFarmBox.setOpenboxnum(0);
            playerFarmBox.setRefreshDate(DateUtil.getNextCountTime());
            saveBean(playerFarmBox);
        }
        return playerFarmBox;
    }
}
