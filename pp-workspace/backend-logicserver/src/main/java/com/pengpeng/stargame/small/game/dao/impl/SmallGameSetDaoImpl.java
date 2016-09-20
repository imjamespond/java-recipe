package com.pengpeng.stargame.small.game.dao.impl;

import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.small.game.dao.ISmallGameSetDao;
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
@DaoAnnotation(prefix = "small.game.set")
public class SmallGameSetDaoImpl extends RedisZSetDao implements ISmallGameSetDao {

}
