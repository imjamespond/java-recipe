package com.pengpeng.stargame.giftbag.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.giftbag.dao.IGiftBagDao;
import com.pengpeng.stargame.model.giftBag.PlayerGiftGag;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 下午12:00
 */
@Component
@DaoAnnotation(prefix = "giftbag.")
public class GiftBagDaoImpl extends RedisDao<PlayerGiftGag> implements IGiftBagDao {
    @Override
    public Class<PlayerGiftGag> getClassType() {
        return PlayerGiftGag.class;
    }

    @Override
    public PlayerGiftGag getPlayerGiftBag(String pid) {
        PlayerGiftGag playerGiftGag=getBean(pid);
        if(playerGiftGag==null){
            playerGiftGag=new PlayerGiftGag(pid);
            saveBean(playerGiftGag);
            return playerGiftGag;
        }
        return playerGiftGag;
    }
}
