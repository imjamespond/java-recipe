package com.pengpeng.stargame.vip.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.vip.PlayerVip;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午4:43
 */
public interface IVipDao extends BaseDao<String,PlayerVip>{
    /**
     *
     * @param pid
     * @return
     */
    public PlayerVip getPlayerVip(String pid);
}
