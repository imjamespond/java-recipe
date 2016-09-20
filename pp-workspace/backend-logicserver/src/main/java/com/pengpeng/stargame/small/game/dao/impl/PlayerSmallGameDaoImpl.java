package com.pengpeng.stargame.small.game.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;
import com.pengpeng.stargame.small.game.dao.IPlayerSmallGameDao;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.small.game.")
public class PlayerSmallGameDaoImpl extends RedisDao<PlayerSmallGame>  implements IPlayerSmallGameDao {

    @Override
    public Class<PlayerSmallGame> getClassType() {
        return PlayerSmallGame.class;
    }

    @Override
    public PlayerSmallGame getPlayerSmallGame(String pid) {
        PlayerSmallGame playerSmallGame=this.getBean(pid);
        if(null == playerSmallGame){
            playerSmallGame = new PlayerSmallGame();
            playerSmallGame.setPid(pid);
            saveBean(playerSmallGame);
        }

        if(null == playerSmallGame.getLastLoginTime()){
            playerSmallGame.setLastLoginTime(new Date(0));
        }
        if(null == playerSmallGame.getFreeTime()){
            playerSmallGame.setFreeTime(new HashMap<Integer, Integer>());
        }
        if(null == playerSmallGame.getGoldTime()){
            playerSmallGame.setGoldTime(new HashMap<Integer, Integer>());
        }
        if(null == playerSmallGame.getScore()){
            playerSmallGame.setScore(new HashMap<Integer, Integer>());
        }
        if(null == playerSmallGame.getScoreWeek()){
            playerSmallGame.setScoreWeek(new HashMap<Integer, Integer>());
        }
        if(null == playerSmallGame.getBuyMap()){
            playerSmallGame.setBuyMap(new HashMap<Integer,Date>());
        }
        return playerSmallGame;
    }

}
