package com.pengpeng.stargame.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.model.Indexable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * User: mql
 * Date: 13-12-10
 * Time: 下午1:41
 */
@Component()
public class RedisKeyValueDB {
    @Autowired
    private RedisDB redisDB;

    public RedisDB getRedisDB() {
        return redisDB;
    }

    public void setRedisDB(RedisDB redisDB) {
        this.redisDB = redisDB;
    }

    /**
     * 保存String类型数据
     * @param key
     * @param value
     */
    public void save(String key,String value){
        redisDB.getRedisTemplate(key).boundValueOps(key).set(value);
    }

    /**
     *删除Key
     * @param key
     */
    public void delete(String key){
        redisDB.getRedisTemplate(key).delete(key);
    }

    /**
     * 获取
     * @param key
     * @return
     */
    public String get(String key){
        String obj =  redisDB.getRedisTemplate(key).boundValueOps(key).get();
        return obj;
    }

    /**
     * 增长
     * @param key
     * @param num
     */
    public void incrBy(final String key, final long num){

        redisDB.getRedisTemplate(key).execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Long all=redisConnection.incrBy(key.getBytes(),num);
                return all;
            }
        }) ;
    }

    /**
     * 批量获取
     * @param keys
     * @return
     */
    public List<String> mGet(final Set<String> keys){
        Set<RedisTemplate  <String, String> >  redisTemplateSet=new HashSet<RedisTemplate<String, String>>();
        for(String key:keys){
            redisTemplateSet.add( redisDB.getRedisTemplate(key));
        }
        final List<String> list=new ArrayList<String>();
        for(final RedisTemplate<String,String> redisTemplate:redisTemplateSet){
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    List<byte[]> bytes=new ArrayList<byte[]>();
                    for(String key:keys){
                        bytes.add(redisTemplate.getStringSerializer().serialize(key));
                    }
                    List<byte[]> listByte=redisConnection.mGet(bytes.toArray(new byte[0][0]));
                    for(byte[] bytes1:listByte){
                        if(bytes1==null){
                            continue;
                        }
                        String obj=redisTemplate.getStringSerializer()
                                .deserialize(bytes1);
//                        datalist.add(gson.fromJson(obj, getClassType()));
                        list.add(obj);
                    }
                    return null;
                }
            }) ;
        }
        return list;

    }

    /**
     * 像Map中插入数据
     * @param key
     * @param index
     * @param value
     */
    public void insertToMap(String key,String index,String value){
        HashOperations<String,String,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        ops.put(key, index, value);
    }

    /**
     * 获取Map中的 一个元素
     * @param key
     * @param index
     * @return
     */
    public String getMapByIndex(String key,String index){
        HashOperations<String,String,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        return ops.get(key,index);

    }

    /**
     * 获取Map的 长度
     * @param key
     * @return
     */
    public Long sizeMap(String key){
        return  redisDB.getRedisTemplate(key).boundHashOps(key).size();
    }

    /**
     * 删除Map中的一个Key
     * @param key
     * @param index
     */
    public void deleteMapByIndex(String key,final String index){
        HashOperations<String,String,String> ops =  redisDB.getRedisTemplate(key).opsForHash();
        ops.delete(key,index);
    }

    /**
     *  通过正则表达式 获取 Key
     * @param reg
     * @return
     */

    public Set<String> keys(String reg){
        List<RedisTemplate<String,String>> redisTemplateList=redisDB.getAll();
        Set<String> keys=new HashSet<String>();
        for(RedisTemplate<String,String> redisTemplate:redisTemplateList){
            keys.addAll(redisTemplate.keys(reg));
        }
        return keys;
    }
}
