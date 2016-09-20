package com.pengpeng.stargame.servermanager;


import com.pengpeng.stargame.managed.NodeStatus;
import com.pengpeng.stargame.managed.ServerType;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-18 下午3:21
 */
public class ServerChecker {

    private static Logger logger = Logger.getLogger(ServerChecker.class);

    public void exec() {
        Collection<ServerInstance> instances = ServerInstanceManager.getServerInstances().values();
        Date now = new Date();

        for (ServerInstance instance : instances) {

            //更新 uptime
            final long upTime = ServerManagerUtil.evalMinutes(instance.getStartTime(), new Date());
            instance.setUpTime(upTime);

            if (ServerType.MANAGER.equals(instance.getNodeInfo().getType())) {
                continue;
            }

            //客户端每隔 2 秒钟发一次心跳，如果超过 10 秒钟没发心跳过来，那么标识为“停止”
            final long secondsSinceLast = ServerManagerUtil.evalSeconds(now, instance.getBeatTime());
            if (secondsSinceLast > 10) {
                if (!NodeStatus.STOPPED.getCode().equals(instance.getStatus())) {
                    logger.info("节点 " + instance.getInstanceId() + " 没响应，现标识为停止");
                    instance.setStatus(NodeStatus.STOPPED.getCode());
                    ServerInstanceManager.update(instance);
                    ServerInstanceManager.broadcastStop(instance.getNodeInfo());
                }
            }

            //如果超过 1 小时都没有心跳，那么直接从列表中剔除
            if (secondsSinceLast > 3600) {
                ServerInstanceManager.removeInstance(instance.getNodeInfo());
            }
        }
    }

}
