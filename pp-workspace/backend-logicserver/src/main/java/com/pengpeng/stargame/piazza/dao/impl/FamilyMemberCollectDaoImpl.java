package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberCollectDao;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-3-25
 * Time: 上午9:28
 */
@Component
@DaoAnnotation(prefix = "pza.family.collect.member.")
public class FamilyMemberCollectDaoImpl extends RedisZSetDao implements IFamilyMemberCollectDao {
}
