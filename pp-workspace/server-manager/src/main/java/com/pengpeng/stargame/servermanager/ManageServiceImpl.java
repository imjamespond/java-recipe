package com.pengpeng.stargame.servermanager;

import com.pengpeng.stargame.managed.IClientService;
import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.IMonitorService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.NodeStatus;
import org.apache.log4j.Logger;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.util.*;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-12 下午4:26
 */
public class ManageServiceImpl implements IManageService {

    private static Logger logger = Logger.getLogger(ManageServiceImpl.class);

    @Override
    public List<NodeInfo> getServerNodes() {
        Map<String, ServerInstance> serverInstances = ServerInstanceManager.getServerInstances();
        Collection<ServerInstance> all = serverInstances.values();
        List<NodeInfo> nodes = new ArrayList<NodeInfo>();
        for (ServerInstance instance : all) {
            if (instance.getStatus().equals(NodeStatus.ONSERVICE.getCode())) {
                nodes.add(instance.getNodeInfo());
            }
        }
        return nodes;
    }

    @Override
    public NodeInfo getServerNode(String serverType) {
        List<NodeInfo> all = getServerNodes(serverType);
        int random = new Random().nextInt(all.size());
        return all.get(random);
//        for(NodeInfo nodeInfo : all){
//            if(nodeInfo.getType().equals(serverType)){
//                return nodeInfo;
//            }
//        }
    }

    @Override
    public NodeInfo getServerNodeById(String serverId) {
        List<NodeInfo> all = getServerNodes();
        for(NodeInfo nodeInfo : all){
            if(nodeInfo.getId().equals(serverId)){
                return nodeInfo;
            }
        }
        return null;
    }

    @Override
    public List<NodeInfo> getServerNodes(String serverType) {
        List<NodeInfo> all = getServerNodes();
        List<NodeInfo> expects = new ArrayList<NodeInfo>();
        for(NodeInfo nodeInfo : all){
            if(nodeInfo.getType().equals(serverType)){
                expects.add(nodeInfo);
            }
        }
        return expects;
    }
}
