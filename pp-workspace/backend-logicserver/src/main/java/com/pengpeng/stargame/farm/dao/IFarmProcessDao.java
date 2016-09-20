package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午4:55
 */
public interface IFarmProcessDao extends BaseDao<String,FarmProcessQueue> {

    public FarmProcessQueue getFarmProcessQueue(String pid);
}
