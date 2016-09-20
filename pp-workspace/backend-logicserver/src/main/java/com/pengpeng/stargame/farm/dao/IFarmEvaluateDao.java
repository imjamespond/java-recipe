package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmEvaluate;

/**
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:44
 */
public interface IFarmEvaluateDao extends BaseDao<String,FarmEvaluate> {

    public FarmEvaluate getFarmEvaluate(String pid);
}
