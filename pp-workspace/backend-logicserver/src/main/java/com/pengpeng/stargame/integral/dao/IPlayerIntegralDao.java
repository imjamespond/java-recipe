package com.pengpeng.stargame.integral.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.integral.PlayerIntegralShow;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:56
 */
public interface IPlayerIntegralDao extends BaseDao<String,PlayerIntegralShow> {
    PlayerIntegralShow getPlayerIntegralShow(String pid);
}
