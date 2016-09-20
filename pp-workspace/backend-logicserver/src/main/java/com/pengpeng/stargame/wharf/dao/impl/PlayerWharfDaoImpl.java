package com.pengpeng.stargame.wharf.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.wharf.PlayerWharf;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.wharf.")
public class PlayerWharfDaoImpl extends RedisDao<PlayerWharf>  implements IPlayerWharfDao {

    @Override
    public Class<PlayerWharf> getClassType() {
        return PlayerWharf.class;
    }

    @Override
    public PlayerWharf getPlayerWharf(String pid) {
        PlayerWharf playerWharf=this.getBean(pid);
        if(null == playerWharf){
            playerWharf = new PlayerWharf();
            playerWharf.setPid(pid);
            playerWharf.setShipArrived(false);
            playerWharf.setRefreshTime(new Date(0));
            playerWharf.setEnable(false);
            playerWharf.setEnableDate(new Date(0));
            saveBean(playerWharf);
        }
        return playerWharf;
    }

}
