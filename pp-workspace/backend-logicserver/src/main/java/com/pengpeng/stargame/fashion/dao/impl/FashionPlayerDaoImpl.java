package com.pengpeng.stargame.fashion.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.fashion.FashionBuilder;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.model.room.FashionPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-29
 * Time: 上午9:54
 */
@Component
@DaoAnnotation(prefix = "fashion.player.")
public class FashionPlayerDaoImpl extends RedisDao<FashionPlayer> implements IFashionPlayerDao {
    @Autowired
    private FashionBuilder fashionBuilder;

    @Override
    public Class<FashionPlayer> getClassType() {
        return FashionPlayer.class;
    }

    @Override
    public FashionPlayer getFashionPlayer(String pId) {
        FashionPlayer fashionPlayer = this.getBean(pId);
        if (fashionPlayer == null) {
            fashionPlayer = fashionBuilder.newFashionPlayer(pId);
            saveBean(fashionPlayer);
        }
        return fashionPlayer;
    }
}
