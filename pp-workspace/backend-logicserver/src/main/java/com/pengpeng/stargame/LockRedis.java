package com.pengpeng.stargame;

import com.pengpeng.stargame.dao.RedisDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * User: mql
 * Date: 13-12-25
 * Time: 上午10:57
 */
@Component("lockRedis")
public class LockRedis implements ILock {

    @Autowired
    private RedisDB redisDB;

    //加锁标志
    public static final String LOCKED = "TRUE";
    //锁Key 前缀
    private static final String LOCKE_KEY = "lock.";
    public static final long ONE_MILLI_NANOS = 1000000000l;
    //默认超时时间（毫秒）
    public static final long DEFAULT_TIME_OUT = 3000;
    public static final Random r = new Random();
    //锁的超时时间（秒），过期删除
    public static final int EXPIRE = 10;

    @Override
    public boolean lock(String id) {
        return lock(5, LOCKE_KEY + id);
    }

    public boolean lock(long timeout, String key) {
        long nano = System.nanoTime();
        //超时时间 如果 超过多少秒 取消加锁
        timeout *= ONE_MILLI_NANOS;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (redisDB.getRedisTemplate(key).boundValueOps(key).setIfAbsent(LOCKED)) {
                    redisDB.getRedisTemplate(key).boundValueOps(key).expire(EXPIRE, TimeUnit.SECONDS);
                    return true;
                }
                // 短暂休眠，nano避免出现活锁
                Thread.sleep(3, r.nextInt(500));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void unlock(String id) {
        String key = LOCKE_KEY + id;
        redisDB.getRedisTemplate(key).delete(key);
    }
}
