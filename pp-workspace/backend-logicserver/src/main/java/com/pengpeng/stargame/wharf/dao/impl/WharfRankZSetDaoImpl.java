package com.pengpeng.stargame.wharf.dao.impl;

import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.IZSetDao;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.small.game.dao.ISmallGameSetDao;
import com.pengpeng.stargame.wharf.dao.IWharfRankSetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "wharf.rank.set")
public class WharfRankZSetDaoImpl extends RedisZSetDao implements IWharfRankSetDao {

}
