package com.pengpeng.stargame.stall.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.stall.PlayerShelf;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
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
@DaoAnnotation(prefix = "player.stall.")
public class PlayerStallDaoImpl extends RedisDao<PlayerStall> implements IPlayerStallDao {

    @Override
    public Class<PlayerStall> getClassType() {
        return PlayerStall.class;
    }

    @Override
    public PlayerStall getPlayerStall(String pid) {
        PlayerStall playerStall=this.getBean(pid);
        if(null == playerStall){
            playerStall = new PlayerStall();
            playerStall.setPid(pid);
            playerStall.setDay(1);

            saveBean(playerStall);
        }

        if(null==playerStall.getLoginDate()){
            playerStall.setLoginDate(new Date(0));
        }

        if(null==playerStall.getMomShelfDate()){
            playerStall.setMomShelfDate(new Date(0));
        }

        if(null==playerStall.getBuyingDate()){
            playerStall.setBuyingDate(new Date(0));
        }

/*        if(null==playerStall.getAssistantTime()){
            playerStall.setAssistantTime(new Date(StallConstant.ASSISTANT_TRIAL + System.currentTimeMillis()));
        }*/

        if(null==playerStall.getAssistantNextTime()){
            playerStall.setAssistantNextTime(new Date(0));
        }


        if(null == playerStall.getPlayerShelfs()){
            playerStall.setPlayerShelfs(new PlayerShelf[StallConstant.SHELF_NUM]);
        }
        if(null == playerStall.getPlayerGoldShelf()){
            playerStall.setPlayerGoldShelf(new ArrayList<PlayerShelf>());
        }
        if(null == playerStall.getPlayerFriShelf()){
            playerStall.setPlayerFriShelf(new ArrayList<PlayerShelf>());
        }
        if(null == playerStall.getPlayerMomShelf()){
            playerStall.setPlayerMomShelf(new ArrayList<PlayerShelf>());
        }
        if(null == playerStall.getPlayerVipShelf()){
            playerStall.setPlayerVipShelf(new ArrayList<PlayerShelf>());
        }
        return playerStall;
    }

}
