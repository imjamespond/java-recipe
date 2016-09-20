package com.pengpeng.stargame.success.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午12:17
 */
public interface IPlayerSuccessDao extends BaseDao<String,PlayerSuccessInfo> {

    public PlayerSuccessInfo getPlayerSuccessInfo(String pid);

}
