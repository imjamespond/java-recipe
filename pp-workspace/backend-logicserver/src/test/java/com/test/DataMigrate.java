package com.test;

import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.model.player.EventLog;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: mql
 * Date: 13-9-5
 * Time: 下午4:59
 */
@Ignore
public class DataMigrate {
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context-logicserver.xml", "context-jredis.xml", "beanRefRuleDataAccess.xml"});
            /**
             * 数据源 Redis
             */
            RedisTemplate<String, String> redisTemplate = (RedisTemplate<String, String>) ctx.getBean("redisTemplate");
            Set<String> keys = redisTemplate.keys("*");
            System.out.println("..................key size " + keys.size());
            /**
             * Reids 路由 类
             */
            RedisDB redisDB = (RedisDB) ctx.getBean("redisDB");
            int stringkey = 0;
            int listkey = 0;
            int hashmapkey = 0;
            int zsetkey = 0;
            System.out.println(" stringkey: " + stringkey + " listkey: " + listkey + "  hashMapkey: " + hashmapkey + " zsetkey: " + zsetkey);
            for (String key : keys) {
                String keytype = redisTemplate.type(key).toString();
                if (keytype.equals("STRING")) {
                    stringkey ++;
                }
                if (keytype.equals("ZSET")) {
                    zsetkey ++;
                }
                if (keytype.equals("LIST")) {
                    listkey ++;
                }
                if (keytype.equals("HASH")) {
                    hashmapkey  ++;
                }
            }
            System.out.println(" stringkey: " + stringkey + " listkey: " + listkey + "  hashMapkey: " + hashmapkey + " zsetkey: " + zsetkey);
            System.out.println(" start  ............." + System.currentTimeMillis());
            Long start = System.currentTimeMillis();
            for (String key : keys) {

                if(redisDB.getRedisTemplate(key).hasKey(key)){
                    redisDB.getRedisTemplate(key).delete(key);
                }

                String keytype = redisTemplate.type(key).toString();
                if (keytype.equals("STRING")) {
                   String value=redisTemplate.boundValueOps(key).get();
                   redisDB.getRedisTemplate(key).boundValueOps(key).set(value);
                }
                if (keytype.equals("ZSET")) {
                    Long size = redisTemplate.boundZSetOps(key).size();
                    Set<String> values = redisTemplate.boundZSetOps(key).reverseRange(0, size);
                    for (String value : values) {
                        double score = redisTemplate.boundZSetOps(key).score(value);
                        redisDB.getRedisTemplate(key).boundZSetOps(key).add(value, score);
                    }
                }
                if (keytype.equals("LIST")) {
                    Long size = redisTemplate.boundListOps(key).size();
                    List<String> values = redisTemplate.boundListOps(key).range(0, size);
                    for (String value : values) {
                        redisDB.getRedisTemplate(key).boundListOps(key).rightPush(value);
                    }

                }
                if (keytype.equals("HASH")) {
                    BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);
                    Map<String, String> items = ops.entries();

                    HashOperations<String, String, String> opsTarget = redisDB.getRedisTemplate(key).opsForHash();
                    for (Map.Entry<String, String> entry : items.entrySet()) {
                        /**
                         * 添加到 目标 Redis
                         */
                        opsTarget.put(key, entry.getKey(), entry.getValue());
                    }
                }


            }
            Long end = System.currentTimeMillis();
            System.out.println(" end   ............." + System.currentTimeMillis());
            System.out.println(" 用时： " + (end - start) / (1000 *3600) + " 分");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
