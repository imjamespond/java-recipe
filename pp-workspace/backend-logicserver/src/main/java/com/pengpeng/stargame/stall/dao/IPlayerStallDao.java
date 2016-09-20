package com.pengpeng.stargame.stall.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.wharf.PlayerWharf;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerStallDao extends BaseDao<String,PlayerStall> {

    public PlayerStall getPlayerStall(String pid);

}
