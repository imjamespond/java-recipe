package com.pengpeng.stargame.wharf.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.wharf.PlayerWharf;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerWharfDao extends BaseDao<String,PlayerWharf> {

    public PlayerWharf getPlayerWharf(String pid);

}
