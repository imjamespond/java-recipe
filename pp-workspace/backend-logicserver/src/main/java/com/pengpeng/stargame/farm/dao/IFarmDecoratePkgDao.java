package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;

/**
 * User: mql
 * Date: 14-3-17
 * Time: 下午4:03
 */
public interface IFarmDecoratePkgDao  extends BaseDao<String,FarmDecoratePkg> {

    public FarmDecoratePkg getFarmDecoratePkg(String pid);
}
