package com.pengpeng.stargame.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.datasave.IAsyDataDao;
import com.pengpeng.stargame.model.Indexable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-11下午12:06
 */
public abstract class RedisDao<ItemType extends Indexable<String>> implements BaseDao<String, ItemType>{
    @Autowired
    private RedisDB redisDB;
    @Autowired
    private IAsyDataDao asyDataDao;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public RedisDao(){
    }
    public abstract Class<ItemType> getClassType();
//    public abstract String getPrefixKey();


    protected String getKey(String index){
        String prefix = AnnotationUtils.getAnno(getClass(),DaoAnnotation.class).prefix();
        String key = prefix+index;
        return key;
    }
    public void saveBean(final ItemType bean){
        final String key = getKey(bean.getKey());
        redisDB.getRedisTemplate(key).boundValueOps(key).set(gson.toJson(bean));
        /**
         * 变化的Key 添加到异步保存队列
         */
        asyDataDao.put(key);
    }
    public void deleteBean(final String index){
        final String key = getKey(index);
        redisDB.getRedisTemplate(key).delete(key);
    }
    public ItemType getBean(String index){
        final String key = getKey(index);
        /**
         * 如果 缓存没有 从数据库内查询
         */
        asyDataDao.dbToRedis(key);
        if (! redisDB.getRedisTemplate(key).hasKey(key)){
             return null;
        }
        String obj =  redisDB.getRedisTemplate(key).boundValueOps(key).get();
        return gson.fromJson(obj,getClassType());
    }

    public Map<String,ItemType> mGet(final Set<String> keys){
        Set<RedisTemplate  <String, String> >  redisTemplateSet=new HashSet<RedisTemplate<String, String>>();
        for(String key:keys){
            redisTemplateSet.add( redisDB.getRedisTemplate(key));
        }
        final Map<String,ItemType> dataMap=new HashMap<String, ItemType>();
        for(final RedisTemplate<String,String> redisTemplate:redisTemplateSet){
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    List<byte[]> bytes=new ArrayList<byte[]>();
                    for(String key:keys){
                        String a=getKey(key);
                        bytes.add(redisTemplate.getStringSerializer().serialize(getKey(key)));
                    }
                    List<byte[]> listByte=redisConnection.mGet(bytes.toArray(new byte[0][0]));
                    for(byte[] bytes1:listByte){
                        if(bytes1==null){
                            continue;
                        }
                        String obj=redisTemplate.getStringSerializer()
                                .deserialize(bytes1);
//                        datalist.add(gson.fromJson(obj, getClassType()));
                        dataMap.put(gson.fromJson(obj, getClassType()).getKey(),gson.fromJson(obj, getClassType()));

                    }
                    return null;
                }
            }) ;
        }
        return dataMap;

    }
    public void updateBean(final String index, final ItemType bean){
        final String key = getKey(index);
        redisDB.getRedisTemplate(key).boundValueOps(key).set(gson.toJson(bean));
    }
}
