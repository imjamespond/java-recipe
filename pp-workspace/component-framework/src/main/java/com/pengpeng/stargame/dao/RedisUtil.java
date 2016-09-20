package com.pengpeng.stargame.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-8下午4:16
 */
public class RedisUtil {
    private static Log log = LogFactory.getLog(RedisUtil.class);

    private int maxActive;
    private int maxIdle;
    private int maxWait;
    private String address;
    private ShardedJedis shardedJedis;

    private JedisPoolConfig jpc= new JedisPoolConfig();
    private ShardedJedisPool pool;

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 初始化 Redis
     */
    protected void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWait(maxWait);
        config.setTestOnBorrow(false);

        List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
        String[] addressArr = address.split(",");
        for (String str : addressArr) {
            list.add(new JedisShardInfo(str.split(":")[0], Integer.parseInt(str.split(":")[1])));
        }
        pool = new ShardedJedisPool(jpc,list);
    }
    /**
     * 关闭 Redis
     */
    protected void destory(){
        pool.destroy();
    }

    /**
     * redis的List集合 ，向key这个list添加元素
     * @param key List别名
     * @param string 元素
     * @return
     */
    public long rpush(String key,String string){
        try{
            shardedJedis = pool.getResource();
            long ret = shardedJedis.rpush(key, string);
            pool.returnResource(shardedJedis);
            return ret;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取key这个List，从第几个元素到第几个元素
     * LRANGE key start stop返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * @param key List别名
     * @param start 开始下标
     * @param end 结束下标
     * @return
     */
    public List<String> lrange(String key,long start,long end){
        try{
            shardedJedis = pool.getResource();
            List<String> ret = shardedJedis.lrange(key, start, end);
            pool.returnResource(shardedJedis);
            return ret;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 将哈希表key中的域field的值设为value。
     * @param key 哈希表别名
     * @param field 键
     * @param value 值
     */
    public void hset(String key,String field,String value){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.hset(key, field, value);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 向key赋值
     * @param key
     * @param value
     */
    public void set(String key ,String value){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.set(key, value);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取key的值
     * @param key
     * @return
     */
    public String get(String key){
        try{
            shardedJedis = pool.getResource();
            String value = shardedJedis.get(key);
            pool.returnResource(shardedJedis);
            return value;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 将多个field - value(域-值)对设置到哈希表key中。
     * @param key
     * @param map
     */
    public void hmset(String key,Map<String, String> map){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.hmset(key, map);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 给key赋值，并生命周期设置为seconds
     * @param key
     * @param seconds 生命周期 秒为单位
     * @param value
     */
    public void setex(String key, int seconds, String value){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.setex(key,seconds,value);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 为给定key设置生命周期
     * @param key
     * @param seconds 生命周期 秒为单位
     */
    public void expire(String key, int seconds){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.expire(key,seconds);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查key是否存在
     * @param key
     * @return
     */
    public boolean exists(String key){
        try{
            shardedJedis = pool.getResource();
            boolean bool = shardedJedis.exists(key);
            pool.returnResource(shardedJedis);
            return bool;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回key值的类型  none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
     * @param key
     * @return
     */
    public String type(String key){
        try{
            shardedJedis = pool.getResource();
            String type = shardedJedis.type(key);
            pool.returnResource(shardedJedis);
            return type;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 从哈希表key中获取field的value
     * @param key
     * @param field
     */
    public String hget(String key,String field){
        try{
            shardedJedis = pool.getResource();
            String value = shardedJedis.hget(key, field);
            pool.returnResource(shardedJedis);
            return value;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回哈希表key中，所有的域和值
     * @param key
     * @return
     */
    public Map<String,String> hgetAll(String key){
        try{
            shardedJedis = pool.getResource();
            Map<String,String> map = shardedJedis.hgetAll(key);
            pool.returnResource(shardedJedis);
            return map;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }

    }

    /**
     * 返回哈希表key中，所有的域和值
     * @param key
     * @return
     */
    public Set<?> smembers(String key){
        try{
            shardedJedis = pool.getResource();
            Set<?> set = shardedJedis.smembers(key);
            pool.returnResource(shardedJedis);
            return set;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除集合中的member元素
     * @param key List别名
     * @param field 键
     */
    public void delSetObj(String key,String field){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.srem(key,field);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断member元素是否是集合key的成员。是（true），否则（false）
     * @param key
     * @param field
     * @return
     */
    public boolean isNotField(String key, String field){
        try{
            shardedJedis = pool.getResource();
            boolean bool = shardedJedis.sismember(key,field);
            pool.returnResource(shardedJedis);
            return bool;
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果key已经存在并且是一个字符串，将value追加到key原来的值之后
     * @param key
     * @param value
     */
    public void append(String key, String value){
        try{
            shardedJedis = pool.getResource();
            shardedJedis.append(key,value);
            pool.returnResource(shardedJedis);
        }catch(Exception e){
            log.error(e);
            if(shardedJedis!=null){
                pool.returnBrokenResource(shardedJedis);
            }
        }
    }
}
