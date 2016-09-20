package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-16上午9:47
 */
@Component()
@DaoAnnotation(prefix = "uid.")
public class UserDao {


    @Autowired
    private RedisDB redisDB;

    public UserDao(){
    }



    /**
     * 根据网站id,取得playerid
     * @param userId
     * @return
     */
    public String getPlayerByUserId(Integer userId){
        String uid = "uid."+String.valueOf(userId);
        String pid = redisDB.getRedisTemplate(uid).boundValueOps(uid).get();
        return pid;
    }

    public void createPlayerId(Integer userId,String pid){
        String uid = "uid."+String.valueOf(userId);
        redisDB.getRedisTemplate(uid).boundValueOps(uid).set(pid);
    }

    public void deleteUserId(int userId) {
        redisDB.getRedisTemplate(String.valueOf(userId)).delete(String.valueOf(userId));
    }
}
