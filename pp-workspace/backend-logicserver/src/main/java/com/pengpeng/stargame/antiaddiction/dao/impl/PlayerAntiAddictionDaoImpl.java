package com.pengpeng.stargame.antiaddiction.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.antiaddiction.dao.IPlayerAntiAddictionDao;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.antiaddiction.PlayerAntiAddiction;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.antiaddiction.")
public class PlayerAntiAddictionDaoImpl extends RedisDao<PlayerAntiAddiction>  implements IPlayerAntiAddictionDao {

    @Override
    public Class<PlayerAntiAddiction> getClassType() {
        return PlayerAntiAddiction.class;
    }

    @Override
    public PlayerAntiAddiction getPlayerAntiAddiction(String pid) {
        PlayerAntiAddiction player=this.getBean(pid);
        if(null == player){
            player = new PlayerAntiAddiction();
            player.setPid(pid);
            saveBean(player);
        }
        return player;
    }


}
