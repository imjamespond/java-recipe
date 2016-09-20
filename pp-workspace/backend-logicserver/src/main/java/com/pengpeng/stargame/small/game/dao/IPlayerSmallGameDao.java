package com.pengpeng.stargame.small.game.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerSmallGameDao extends BaseDao<String,PlayerSmallGame> {

    public PlayerSmallGame getPlayerSmallGame(String pid);

}
