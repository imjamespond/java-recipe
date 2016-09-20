package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.box.PlayerFarmBox;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午3:00
 */
public interface IFarmBoxDao extends BaseDao<String,PlayerFarmBox> {
    public PlayerFarmBox getPlayerFarmBox(String pid);
}
