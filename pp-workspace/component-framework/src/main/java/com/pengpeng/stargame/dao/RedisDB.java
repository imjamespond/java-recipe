package com.pengpeng.stargame.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-9-5
 * Time: 下午2:23
 */
@Component()
public class RedisDB {
    /**
     * 0 测试环境     1 正式环境
     */
    private final Logger logger = Logger.getLogger(getClass());
    protected RedisTemplate<String, String> redisTemplate;
    private int environment;
    private int serverNum;
    private List<RedisTemplate<String,String>> redisTemplateList;

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public int getEnvironment() {
        return environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }

    public int getServerNum() {
        return serverNum;
    }

    public void setServerNum(int serverNum) {
        this.serverNum = serverNum;
    }

    public List<RedisTemplate<String, String>> getRedisTemplateList() {
        return redisTemplateList;
    }



    public void setRedisTemplateList(List<RedisTemplate<String, String>> redisTemplateList) {
        this.redisTemplateList = redisTemplateList;
    }

    public RedisTemplate<String, String> getRedisTemplate(String key) {
        if (environment == 1) {

            int index = Math.abs(key.hashCode() % serverNum);
            RedisTemplate  <String, String> redisTemplateT=  redisTemplateList.get(index);

            return redisTemplateT;

        }
        if (environment == 0) {
           return redisTemplate;
        }
        return null;

    }

    public List<RedisTemplate<String,String>> getAll(){
        List<RedisTemplate<String,String>> all=new ArrayList<RedisTemplate<String, String>>();
        if (environment == 1) {
            all.addAll(redisTemplateList);
        }
        if (environment == 0) {
            all.add(redisTemplate);
        }
        return all;
    }


}
