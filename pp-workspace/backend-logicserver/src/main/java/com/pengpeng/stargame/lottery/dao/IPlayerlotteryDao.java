package com.pengpeng.stargame.lottery.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.room.FashionPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IPlayerlotteryDao extends BaseDao<String,PlayerLottery> {

    public PlayerLottery getPlayerLottery(String pid);

    public int getFreeNum();
}
