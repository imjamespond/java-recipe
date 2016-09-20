package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmAction;
import com.pengpeng.stargame.model.farm.FarmActionInfo;
import com.pengpeng.stargame.model.farm.FarmEvaluate;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午11:17
 */
public interface IFarmActionDao extends BaseDao<String,FarmActionInfo> {

    public FarmActionInfo getFarmActionInfo(String pid);
}
