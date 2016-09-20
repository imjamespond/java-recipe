package com.pengpeng.stargame.servermanager;

import com.pengpeng.stargame.managed.IClientService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.NodeStatus;
import com.pengpeng.stargame.managed.ServerType;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器实例管理器.
 * @author ChenHonghong@gmail.com
 * @since 13-4-15 下午3:50
 */
public class ServerInstanceManager {

    public static Map<String,ServerInstance> serverInstances = new ConcurrentHashMap<String,ServerInstance>();

    private static Logger logger = Logger.getLogger(ServerInstanceManager.class);

    /**
     * 添加新实例。
     * @param newNodeInfo 新的节点信息
     */
    public static void addInstance(NodeInfo newNodeInfo){
        String serverId = ServerManagerUtil.buildInstanceId(newNodeInfo);

        if(!serverInstances.containsKey(serverId)){
            final Date now = new Date();
            ServerInstance serverInstance = new ServerInstance();
            serverInstance.setNodeInfo(newNodeInfo);
            serverInstance.setBeatTime(now);
            serverInstance.setInstanceId(serverId);
            serverInstance.setStatus(NodeStatus.ONSERVICE.getCode());
            serverInstance.setStartTime(now);
            serverInstances.put(serverId, serverInstance);
        }
    }

    /**
     * 暂停一个实例。
     * @param instanceId 实例ID
     */
    public static void pause(String instanceId){
        ServerInstance instance = getServerInstance(instanceId);
        if(instance == null){
            return;
        }

        if(isManager(instanceId)){
            return;
        }

        instance.setStatus(NodeStatus.PAUSED.getCode());

        //调用客户端的暂停接口
        IClientService nodeClient = instance.getNodeClient();
        nodeClient.pause();
    }

    /**
     * 恢复正常服务.
     * @param serverId 内部ID
     */
    public static void resume(String serverId){
        ServerInstance instance = getServerInstance(serverId);
        if(instance == null){
            return;
        }

        if(NodeStatus.PAUSED.getCode().equals(instance.getStatus())){
            //暂停状态，只能由管理员手工恢复
            instance.setStatus(NodeStatus.ONSERVICE.getCode());

            //调用客户端的停止接口
            IClientService nodeClient = instance.getNodeClient();
            nodeClient.resume();
        }

    }

    /**
     * 停止某个节点。
     * @param serverId 内部ID
     */
    public static void stop(String serverId){
        ServerInstance instance = getServerInstance(serverId);
        if(instance == null){
            return;
        }

        //管理器不能停止
        if(isManager(serverId)){
            return;
        }

        //调用客户端的停止接口
        IClientService nodeClient = instance.getNodeClient();
        nodeClient.stop();

        //将实例状态修改为“已停止”
        instance.setStatus(NodeStatus.STOPPED.getCode());

        //通过的有服务“该服务”已停止
        broadcastStop(instance.getNodeInfo());
    }

    public static void broadcastStop(NodeInfo stoppedNode){
        List<ServerInstance> instances = getInstances();
        for(ServerInstance serverInstance : instances){
            if(serverInstance.getStatus().equals(NodeStatus.STOPPED.getCode())){
                continue;
            }
            serverInstance.getNodeClient().notifyNodeStop(stoppedNode);
        }
    }

    public static ServerInstance getServerInstance(String instanceId){
        return serverInstances.get(instanceId);
    }

    /**
     * 检查该服务器是否是一个服务器管理器。
     * @param serverId
     * @return
     */
    public static boolean isManager(String serverId){
        if(ServerType.MANAGER.equals(getType(serverId))){
            return true;
        }
        return false;
    }

    public static String getStatus(String serverId){
        ServerInstance instance = getServerInstance(serverId);
        return instance.getStatus();
    }

    public static String getType(String serverId){
        ServerInstance instance = getServerInstance(serverId);
        return instance.getNodeInfo().getType();
    }

    public static void update(ServerInstance instance){
        serverInstances.remove(instance.getInstanceId());
        serverInstances.put(instance.getInstanceId(),instance);
    }

    public static void removeInstance(NodeInfo nodeInfo){
        String key = ServerManagerUtil.buildInstanceId(nodeInfo);
        serverInstances.remove(key);
    }


    public static Map<String,ServerInstance> getServerInstances(){
        return serverInstances;
    }

    public static List<ServerInstance> getByType(String type){
        List<ServerInstance> all = getInstances();
        List<ServerInstance> expects = new ArrayList<ServerInstance>();
        for(ServerInstance instance : all){
            if(instance.getNodeInfo().getType().equals(type)){
                expects.add(instance);
            }
        }
        return expects;
    }

    public static List<ServerInstance> getInstances(){
        List<ServerInstance> list = new ArrayList<ServerInstance>(serverInstances.values());
        return list;
    }

}
