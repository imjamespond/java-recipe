package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmOrder;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 上午10:32
 */
public interface IFarmOrderDao extends BaseDao<String,FarmOrder> {

    public FarmOrder getFarmOrder(String pId);
}
