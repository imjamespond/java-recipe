package com.pengpeng.stargame.successive.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.successive.dao.IPlayerSuccessiveDao;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.successive.")
public class PlayerSuccessiveDaoImpl extends RedisDao<PlayerSuccessive>  implements IPlayerSuccessiveDao {
    public static int SUCCESSIVENUM = 7;

    @Override
    public Class<PlayerSuccessive> getClassType() {
        return PlayerSuccessive.class;
    }

    @Override
    public PlayerSuccessive getPlayerSuccessive(String pid) {
        PlayerSuccessive playerSuccessive=this.getBean(pid);
        if(null == playerSuccessive){
            playerSuccessive = new PlayerSuccessive();
            playerSuccessive.setPid(pid);
            playerSuccessive.setDay(0);
            playerSuccessive.setGetPrize(0);
            playerSuccessive.setLastLogin(new Date(0));
            saveBean(playerSuccessive);
        }
        return playerSuccessive;
    }

}
