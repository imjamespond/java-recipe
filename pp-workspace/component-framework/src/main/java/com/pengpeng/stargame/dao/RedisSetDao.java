package com.pengpeng.stargame.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.model.Indexable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-11下午12:06
 */
public abstract class RedisSetDao<Index extends Serializable ,ItemType extends Indexable<Index>> implements ISetDao<Index,ItemType> {

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Autowired
    private RedisDB redisDB;

    protected String getKey(String index){
        String prefix = this.getClass().getAnnotation(DaoAnnotation.class).prefix();
        String key = prefix+index;
        return key;
    }

    public abstract Class<ItemType> getClassType();
    public long size(String id){
        final String key = getKey(id);
        return  redisDB.getRedisTemplate(key).boundSetOps(key).size();
    }
    public void insertBean(String id,ItemType bean){
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundSetOps(key).add(gson.toJson(bean));
    }

    public List<ItemType> members(String id){
        final String key = getKey(id);
        Set<String> set = redisDB.getRedisTemplate(key).boundSetOps(key).members();
        List<ItemType> items = new ArrayList<ItemType>();

        for(String str:set){
            items.add(gson.fromJson(str,getClassType()));
        }
        return items;
    }

    public void clean(String id) {
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).delete(key);
    }
    @Override
    public void deleteBean(String id, ItemType bean) {
        final String key = getKey(id);
        redisDB.getRedisTemplate(key).boundSetOps(key).add(gson.toJson(bean));
    }

    @Override
    public boolean isMember(String id, ItemType bean) {
        final String key = getKey(id);
        return redisDB.getRedisTemplate(key).boundSetOps(key).isMember(gson.toJson(bean));
    }

    @Override
    public ItemType getBean(String id, ItemType bean) {
        final String key = getKey(id);
        boolean member =  redisDB.getRedisTemplate(key).boundSetOps(key).isMember(gson.toJson(bean));
        if (member){
            return bean;
        }else{
            return null;
        }
    }

    @Override
    public void updateBean(String id, ItemType bean) {
//        final String key = getKey(id);
//        redisTemplate.boundSetOps(key).add(gson.toJson(bean));
    }
}
