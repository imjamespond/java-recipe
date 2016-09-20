package com.pengpeng.stargame.room.dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.room.dao.IRoomRankDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * User: mql
 * Date: 13-8-12
 * Time: 上午11:19
 */
@Component
@DaoAnnotation(prefix = "room.rank.")
public class RoomRankDaoImpl extends RedisZSetDao implements IRoomRankDao {

}
