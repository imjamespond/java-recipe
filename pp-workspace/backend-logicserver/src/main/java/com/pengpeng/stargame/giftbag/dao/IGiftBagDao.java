package com.pengpeng.stargame.giftbag.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.giftBag.PlayerGiftGag;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 下午12:00
 */
public interface IGiftBagDao extends BaseDao<String,PlayerGiftGag> {
    public PlayerGiftGag getPlayerGiftBag(String pid);
}
