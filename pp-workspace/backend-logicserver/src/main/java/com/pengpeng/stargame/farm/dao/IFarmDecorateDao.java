package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;

/**
 * User: mql
 * Date: 14-3-13
 * Time: 下午4:02
 */
public interface IFarmDecorateDao extends BaseDao<String,FarmDecorate>{
    FarmDecorate getFarmDecorate(String pid);
}
