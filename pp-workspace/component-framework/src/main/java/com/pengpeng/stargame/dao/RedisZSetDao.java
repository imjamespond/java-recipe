package com.pengpeng.stargame.dao;

import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.datasave.IAsyDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
public abstract class RedisZSetDao implements IZSetDao {
    @Autowired
    private RedisDB redisDB;
    @Autowired
    private IAsyDataDao asyDataDao;
    protected String getKey(String index){
        String prefix = AnnotationUtils.getAnno(getClass(), DaoAnnotation.class).prefix();
        String key = prefix+index;
        return key;
    }

    public long size(String id) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        long size = redisDB.getRedisTemplate(key).boundZSetOps(key).size();
        return size;
    }


    public void addBean(String id, String bean,double score) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Double s = getScore(id,bean);
        if(null == s){
            redisDB.getRedisTemplate(key).boundZSetOps(key).add(bean,score);
        } else {
            if(s.intValue()<score)  {
                redisDB.getRedisTemplate(key).boundZSetOps(key).add(bean,score);
            }
        }
        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }

    @Override
    public void addBeanExpire(String id, String bean, double score, int day) {
        final String key = getKey(id);
        addBean( id, bean, score);
        redisDB.getRedisTemplate(key).boundZSetOps(key).expire((long)day, TimeUnit.DAYS);
        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }


    public void removeBean(String id, String bean) {
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundZSetOps(key).remove(bean);
        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }

    public void removeRange(String id,long star,long end) {
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundZSetOps(key).removeRange(star,end);
    }

    public Set<String> getMembers(String id,int start,int end) {
        final String key = getKey(id);
        Set<String> sets = redisDB.getRedisTemplate(key).boundZSetOps(key).reverseRange(start,end);
        return  sets;
    }


    public Set<String> getMembersAsc(String id, int start, int end) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Set<String> sets = redisDB.getRedisTemplate(key).boundZSetOps(key).range(start, end);
        return  sets;
    }


    public boolean contains(String id,String bean){
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Double score = redisDB.getRedisTemplate(key).boundZSetOps(key).score(bean);
        if (score==null){
            return false;
        }
        return true;
    }

    public Double getScore(String id, String bean) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Double score = redisDB.getRedisTemplate(key).boundZSetOps(key).score(bean);
        return score;
    }

    public Long getRank(String id, String bean) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Long rank = redisDB.getRedisTemplate(key).boundZSetOps(key).reverseRank(bean);
        return rank;
    }

    public Set<ZSetOperations.TypedTuple<String>> getReverseRangeWithScores(String id, int start, int end) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Set<ZSetOperations.TypedTuple<String>> set = redisDB.getRedisTemplate(key).boundZSetOps(key).reverseRangeWithScores( start, end);
        return set;
    }

    public Set<String> getMembersByScore(String id,int score,int star,int end) {
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        Set<String> sets = redisDB.getRedisTemplate(key).boundZSetOps(key).rangeByScore(score,score);
        return  sets;
    }

   public void incrementScore(String id,int score,String  bean){
       final String key = getKey(id);
       redisDB.getRedisTemplate(key).boundZSetOps(key).incrementScore(bean,score);
   }




}
