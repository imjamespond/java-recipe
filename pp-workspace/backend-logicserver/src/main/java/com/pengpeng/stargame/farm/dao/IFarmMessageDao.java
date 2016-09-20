package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmMessageInfo;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午11:50
 */
public interface IFarmMessageDao extends BaseDao<String,FarmMessageInfo> {
    public FarmMessageInfo getFarmMessageInfo(String pid);
}
