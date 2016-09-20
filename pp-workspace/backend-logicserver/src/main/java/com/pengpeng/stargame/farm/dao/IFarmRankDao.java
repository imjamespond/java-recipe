package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.IZSetDao;

/**
 * User: mql
 * Date: 13-8-12
 * Time: 上午11:18
 */
public interface IFarmRankDao extends IZSetDao {

    void playerChangeFarmLevel(String pId,double farmLevel);
}
