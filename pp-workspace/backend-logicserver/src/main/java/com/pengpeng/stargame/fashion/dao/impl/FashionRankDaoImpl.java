package com.pengpeng.stargame.fashion.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.farm.dao.IFarmRankDao;
import com.pengpeng.stargame.fashion.dao.IFashionRankDao;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午5:02
 * 时尚值 排行
 */
@Component
@DaoAnnotation(prefix = "fashion.rank.")
public class FashionRankDaoImpl extends RedisZSetDao implements IFashionRankDao {
}
