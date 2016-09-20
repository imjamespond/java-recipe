package com.pengpeng.stargame.antiaddiction.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.antiaddiction.PlayerAntiAddiction;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerAntiAddictionDao extends BaseDao<String,PlayerAntiAddiction> {

    public PlayerAntiAddiction getPlayerAntiAddiction(String pid);

}
