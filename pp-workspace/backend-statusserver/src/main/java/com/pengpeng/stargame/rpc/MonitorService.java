package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.IChannelContainer;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.ServerType;
import com.pengpeng.stargame.managed.SimpleClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-16下午8:54
 */
//@Component("monitorService")//使用配置文件配置
public class MonitorService extends SimpleClientService {

    @Autowired
    private RedisDB redisDB;

    @Autowired
    private IChannelContainer container;

    public MonitorService(){
    }

    @Override
    public void notifyNodeStop(NodeInfo node) {
        System.out.println("stop node.type="+node.getType());
        if (ServerType.LOGIC.equalsIgnoreCase(node.getType())){
            //链接redis清空
            String key = "lock." + node.getId();
            Set<String> sets = redisDB.getRedisTemplate(key).boundSetOps(key).members();
            for(String tempkey:sets){
                redisDB.getRedisTemplate(tempkey).delete(tempkey);
            }
            redisDB.getRedisTemplate(key).delete(key);
        }else if (ServerType.GAMESERVER.equalsIgnoreCase(node.getType())){
            //如果是前台服务器,则情况这个服务器的所有会话
            //TODO:清空gameserver的session信息
            container.cleanByServerId(node.getId());
            System.out.println("GameServer stop id="+node.getId());
        }
        super.notifyNodeStop(node);
    }
}
