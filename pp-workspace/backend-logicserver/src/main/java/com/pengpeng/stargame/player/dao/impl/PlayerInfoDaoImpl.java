package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.lottery.dao.IPlayerlotteryDao;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.player.PlayerInfo;
import com.pengpeng.stargame.player.dao.IPlayerInfoDao;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.info.")
public class PlayerInfoDaoImpl extends RedisDao<PlayerInfo>  implements IPlayerInfoDao {

    @Override
    public Class<PlayerInfo> getClassType() {
        return PlayerInfo.class;
    }

    @Override
    public PlayerInfo getPlayerInfo(String pid) {
        PlayerInfo playerInfo=this.getBean(pid);
        if(null == playerInfo){
            playerInfo = new PlayerInfo();
            playerInfo.setPid(pid);
            saveBean(playerInfo);
        }
        return playerInfo;
    }


}
