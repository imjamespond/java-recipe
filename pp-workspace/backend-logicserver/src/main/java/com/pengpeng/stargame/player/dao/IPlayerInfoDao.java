package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.player.PlayerInfo;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-8下午5:54
 */
public interface IPlayerInfoDao extends BaseDao<String,PlayerInfo> {
    public PlayerInfo getPlayerInfo(String pId);

}
