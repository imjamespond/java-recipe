package com.pengpeng.stargame.lucky.tree.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.lucky.tree.PlayerLuckyTree;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerLuckyTreeDao extends BaseDao<String,PlayerLuckyTree> {


    PlayerLuckyTree getPlayerLuckyTree(String pid);
}
