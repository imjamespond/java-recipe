package com.pengpeng.stargame.stall.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;
import com.pengpeng.stargame.model.stall.StallAdvertisement;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IStallAdvertisementDao extends BaseDao<String,StallAdvertisement> {

    public StallAdvertisement getStallAdvertisement();

}
