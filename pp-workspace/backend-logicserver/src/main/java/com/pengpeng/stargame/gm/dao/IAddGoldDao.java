package com.pengpeng.stargame.gm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.gm.AddGoldInfo;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午12:10
 */
public interface IAddGoldDao extends BaseDao<String,AddGoldInfo> {

    public AddGoldInfo getAddGoldInfo();
}
