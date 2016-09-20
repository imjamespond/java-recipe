package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.player.dao.IPlayerMoneyRankDao;
import com.pengpeng.stargame.player.dao.UserDao;
import com.pengpeng.stargame.model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-16上午9:41
 */
@Component()
@DaoAnnotation(prefix = "p.")
public class PlayerDaoImpl extends RedisDao<Player> implements IPlayerDao {

    @Autowired
    private UserDao userDao;
    @Autowired
    private IPlayerMoneyRankDao playerMoneyRankDao;
    @Override
    public Class<Player> getClassType() {
        return Player.class;
    }


    public void insertBean(Player bean) {
        super.saveBean(bean);
        userDao.createPlayerId(bean.getUserId(),bean.getId());
    }

    @Override
    public void deleteBean(String index) {
        Player player = getBean(index);
        super.deleteBean(index);
        userDao.deleteUserId(player.getUserId());
    }

    public String getPid(Integer userId){
        return userDao.getPlayerByUserId(userId);
    }
    @Override
    public void saveBean(Player bean){
      super.saveBean(bean);
        /**
         * 排行榜的处理
         */
        if(playerMoneyRankDao.contains("",bean.getId())){
            playerMoneyRankDao.removeBean("",bean.getId());
        }
        playerMoneyRankDao.addBean("",bean.getId(),bean.getGameCoin());
    }
}
