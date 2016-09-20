package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.player.dao.IPlayerMoneyRankDao;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午5:20
 */
@Component
@DaoAnnotation(prefix = "player.rank.money")
public class PlayerMoneyRankDaoImpl extends RedisZSetDao implements IPlayerMoneyRankDao {
}
