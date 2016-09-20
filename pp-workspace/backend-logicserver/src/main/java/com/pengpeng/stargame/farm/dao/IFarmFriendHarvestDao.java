package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmFriendHarvest;

/**
 * User: mql
 * Date: 13-4-28
 * Time: 上午11:37
 */
public interface IFarmFriendHarvestDao extends BaseDao<String,FarmFriendHarvest> {
    public FarmFriendHarvest getFarmFriendHarvest(String pId);
}
