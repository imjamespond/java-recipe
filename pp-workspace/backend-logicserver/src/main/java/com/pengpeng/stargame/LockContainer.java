package com.pengpeng.stargame;

import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.managed.NodeConfig;
import com.pengpeng.stargame.rpc.StatusRemote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-2下午9:30
 */
@Component()
public class LockContainer implements ILock {
    private final Logger logger = Logger.getLogger(getClass());


    @Autowired
    private RedisDB redisDB;



    @Override
    public boolean lock(String id) {
        String key = "lock."+id;
        String sid = NodeConfig.getId();
        boolean exists = true;
        int idx = 50;
        while(exists){
            if (idx<=0){
                break;
            }
            synchronized (key){
                try {
                    exists = redisDB.getRedisTemplate(key).hasKey(key);
                    if (exists){
                        key.wait(10);
                        idx--;
                        continue;
                    }else{
                        BoundSetOperations<String,String> bso = redisDB.getRedisTemplate("lock." + sid).boundSetOps("lock." + sid);
                        bso.add(key);
                        bso.expire(5, TimeUnit.SECONDS);
                        BoundValueOperations<String,String> bfo = redisDB.getRedisTemplate(key).boundValueOps(key);
                        bfo.setIfAbsent("lock");
                        bfo.expire(5, TimeUnit.SECONDS);
                        //logger.debug("lock.id:" + key);
                        return true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public void unlock(String id) {
        String key = "lock."+id;
        String sid = NodeConfig.getId();
        BoundSetOperations<String,String> bs = redisDB.getRedisTemplate("lock." + sid).boundSetOps("lock."+sid);
        bs.remove(key);
        bs.expire(5, TimeUnit.SECONDS);
        redisDB.getRedisTemplate(key).delete(key);
        //logger.debug("unlock.id:"+key);
    }

}
