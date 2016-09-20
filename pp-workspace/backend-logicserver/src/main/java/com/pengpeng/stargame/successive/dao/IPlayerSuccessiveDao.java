package com.pengpeng.stargame.successive.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerSuccessiveDao extends BaseDao<String,PlayerSuccessive> {

    public PlayerSuccessive getPlayerSuccessive(String pid);

}
