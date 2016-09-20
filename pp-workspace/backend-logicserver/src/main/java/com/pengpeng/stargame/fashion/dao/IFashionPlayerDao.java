package com.pengpeng.stargame.fashion.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.room.FashionPlayer;

/**
 * User: mql
 * Date: 13-5-29
 * Time: 上午9:53
 */
public interface IFashionPlayerDao extends BaseDao<String,FashionPlayer> {

    public  FashionPlayer getFashionPlayer(String pId);
}
