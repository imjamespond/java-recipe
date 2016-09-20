package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.ILock;
import com.pengpeng.stargame.managed.NodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-15下午2:36
 */
//@Component()
public class LockDaoImpl implements ILock {

    protected RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String id) {
        String sid = NodeConfig.getId();
        boolean exists = true;
        while(exists){
            synchronized (id){
                try {
                    exists = redisTemplate.hasKey(id);
                    if (exists){
                        id.wait(10);
                        continue;
                    }else{
                        redisTemplate.boundSetOps("lock."+sid).add(id);
                        redisTemplate.boundValueOps(id).set("lock",5, TimeUnit.MINUTES);
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
        String sid = NodeConfig.getId();
        redisTemplate.boundSetOps("lock."+sid).remove(id);
        redisTemplate.delete(id);
    }

}
