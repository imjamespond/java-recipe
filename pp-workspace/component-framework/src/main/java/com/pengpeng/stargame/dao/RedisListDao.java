package com.pengpeng.stargame.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.datasave.IAsyDataDao;
import com.pengpeng.stargame.model.Indexable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-11下午12:06
 */
public abstract class RedisListDao<Index extends Serializable ,ItemType extends Indexable<Index>> implements IListDao<Index,ItemType> {

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Autowired
    private RedisDB redisDB;
    @Autowired
    private IAsyDataDao asyDataDao;

    protected String getKey(String index){
        String prefix = this.getClass().getAnnotation(DaoAnnotation.class).prefix();
        String key = prefix+index;
        return key;
    }

    public abstract Class<ItemType> getClassType();
    public long size(String id){
        final String key = getKey(id);

        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        return  redisDB.getRedisTemplate(key).boundListOps(key).size();
    }
    public void insertBean(String id,ItemType bean){
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundListOps(key).leftPush(gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }

    public void lPush(String id,ItemType bean){
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundListOps(key).leftPush(gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }

    public void rPop(String id){
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        redisDB.getRedisTemplate(key).boundListOps(key).rightPop();

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }
    public void saveBean(String id,ItemType bean){
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundListOps(key).leftPush(gson.toJson(bean));
        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }

    public void insertBean(String id,long idx,ItemType bean){
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundListOps(key).set(idx, gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }
    public void saveBean(String id,long idx,ItemType bean){
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundListOps(key).set(idx, gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }

    public void deleteBean(String id ,long idx){
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        redisDB.getRedisTemplate(key).boundListOps(key).remove(idx,null);
        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }
    public ItemType getBean(String id,long idx){
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);


        String obj =  redisDB.getRedisTemplate(key).boundListOps(key).index(idx);
        Class<ItemType> cls = null;
        return gson.fromJson(obj,cls);
    }
    public void updateBean(String id,long idx,ItemType bean){
        final String key = getKey(id);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);


        redisDB.getRedisTemplate(key).boundListOps(key).set(idx,gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }



	@Override
	public List<ItemType> getList(String id) {
        return getList(id,0,10);
	}

	@Override
	public List<ItemType> getList(String id, int begin, int size) {
        final String key = getKey(id);

        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        List<String> items =  redisDB.getRedisTemplate(key).boundListOps(key).range(begin,begin+size);
        List<ItemType> list = new ArrayList<ItemType>(items.size());
        for(String item:items){
            ItemType obj = gson.fromJson(item,getClassType());
            list.add(obj);
        }
        return list;
    }
}
