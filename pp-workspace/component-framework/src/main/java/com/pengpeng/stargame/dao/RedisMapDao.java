package com.pengpeng.stargame.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.datasave.IAsyDataDao;
import com.pengpeng.stargame.manager.BreakException;
import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.Indexable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-11下午12:06
 */
public abstract class RedisMapDao<Index extends Serializable,ItemType extends Indexable<Index>> implements IMapDao<Index,ItemType> {
    @Autowired
    private RedisDB redisDB;
    @Autowired
    private IAsyDataDao asyDataDao;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    protected String getKey(String pid){
        String prefix = this.getClass().getAnnotation(DaoAnnotation.class).prefix();
        String key = prefix+pid;
        return key;
    }
    public abstract Class<ItemType> getClassType();


	public long size(String pid){
		final String key = getKey(pid);

        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

		return  redisDB.getRedisTemplate(key).boundHashOps(key).size();
	}

	public long maxPage(String pid,int size){
		long totalCount = size(pid);
		if(totalCount==0){
			return 0;
		}

		long max = (totalCount %  size == 0 ? totalCount / size : totalCount / size + 1);
		return max;
	}

    public void insertBean(String pid,final ItemType bean){
        final String key = getKey(pid);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);


        HashOperations<String,Index,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        ops.put(key, bean.getKey(), gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }
    public void saveBean(String pid,final ItemType bean){
        final String key = getKey(pid);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        HashOperations<String,Index,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        ops.put(key, bean.getKey(), gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }
    public void deleteBean(String pid,final Index index){
        final String key = getKey(pid);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);
        HashOperations<String,Index,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        ops.delete(key,index);

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }
    public ItemType getBean(String pid,final Index index){
        final String key = getKey(pid);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

        HashOperations<String,Index,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        String obj = ops.get(key,index);
        return gson.fromJson(obj,getClassType());
    }
    public void updateBean( String pid, final ItemType bean){
        final String key = getKey(pid);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);
        HashOperations<String,Index,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        ops.put(key,bean.getKey(),gson.toJson(bean));

        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);

    }

	public HashMap<Index, ItemType> findAll(String pid) {
		final String key = getKey(pid);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);

		BoundHashOperations<String,String,String> ops =  redisDB.getRedisTemplate(key).boundHashOps(key);
        Map<String,String> items = ops.entries();
        HashMap<Index,ItemType> maps = new HashMap<Index, ItemType>(items.size());
        for(Map.Entry<String,String> entry:items.entrySet()){
            ItemType item = gson.fromJson(entry.getValue(),getClassType());
            maps.put(item.getKey(),item);
        }
        return maps;
	}

	public HashMap<Index, ItemType> findPage(String pid, int begin, int size) {
		HashMap<Index, ItemType> maps = new HashMap<Index, ItemType>();

		HashMap<Index, ItemType> mapAll =findAll(pid);
		if(mapAll ==null || mapAll.isEmpty()){
			return null;
		}

		// 开始下标
		int startRow = (begin - 1) * size;

		// 获取多少记录
		int fetchSize = size;

		int i = 0;
		Iterator<Map.Entry<Index,ItemType>> iter = mapAll.entrySet().iterator();
		while (iter.hasNext()) {
			if(fetchSize <=0){
				break;
			}

			if(i == startRow){
				Map.Entry<Index,ItemType> entry = iter.next();
				maps.put(entry.getKey(),entry.getValue());
				fetchSize--;
			}

			i++;
		}
		return maps;
	}

    @Override
    public List<ItemType> find(String pid,IFinder<ItemType> finder) {
        HashMap<Index,ItemType> map = findAll(pid);
        List<ItemType> list = new ArrayList<ItemType>();
        int idx = 0;
        try {
            for(Map.Entry<Index,ItemType> item:map.entrySet()){
                if (finder.match(idx,item.getValue())){
                    list.add(item.getValue());
                }
                idx++;
            }
        } catch (BreakException e) {
        }
        return list;
    }
}
