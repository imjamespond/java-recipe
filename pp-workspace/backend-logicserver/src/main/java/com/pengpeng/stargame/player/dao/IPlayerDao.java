package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.player.Player;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-8下午5:54
 */
public interface IPlayerDao extends BaseDao<String,Player> {
    public String getPid(Integer userId);

    public void saveBean(Player player);

}
