package com.pengpeng.stargame.servermanager;

import com.pengpeng.stargame.managed.IClientService;
import com.pengpeng.stargame.managed.IMonitorService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.NodeStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.util.Date;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-16 下午4:58
 */
public class MonitorServiceImpl implements IMonitorService {

    private static Logger logger = Logger.getLogger(MonitorServiceImpl.class);

    @Override
    public void register(NodeInfo nodeInfo) {
        ServerInstanceManager.removeInstance(nodeInfo);
        ServerInstanceManager.addInstance(nodeInfo);
    }

    @Override
    public void unRegister(NodeInfo nodeInfo) {
        ServerInstanceManager.removeInstance(nodeInfo);
    }

    @Override
    public void onState(NodeInfo nodeInfo, String status) {

        String instanceId = ServerManagerUtil.buildInstanceId(nodeInfo);
        ServerInstance instance = ServerInstanceManager.getServerInstance(instanceId);
        if(instance == null){
            return;
        }

        //更新心跳时间
        instance.setBeatTime(new Date());
        instance.getNodeInfo().setRuntime(nodeInfo.getRuntime());
        BeanUtils.copyProperties(nodeInfo.getRuntime(),instance.getNodeInfo().getRuntime());
        ServerInstanceManager.update(instance);

        //检查 NodeClient
        if(instance.getNodeClient() == null){
            instance.setNodeClient(connectNodeClient(nodeInfo));
        }

        //暂停状态，只能由管理员手工恢复
        if(NodeStatus.PAUSED.getCode().equals(instance.getStatus())){
            return;
        }

        //更新状态
        instance.setStatus(status);

    }

    private IClientService connectNodeClient(NodeInfo nodeInfo) {
        IClientService client = null;
        final String serviceUrl = "rmi://" + nodeInfo.getHost() + ":" + nodeInfo.getPort() + "/NodeClient";
        try {
            logger.info("连接至（"+nodeInfo.getType()+"） " + serviceUrl + " ..." );
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();

            proxy.setServiceUrl(serviceUrl);
            proxy.setServiceInterface(IClientService.class);
            proxy.afterPropertiesSet();
            client = (IClientService)proxy.getObject();
            logger.info("成功连接上 " + serviceUrl);
        } catch (Exception e) {
            logger.warn("无法连接至"+nodeInfo.getType()+"的 NodeClient 接口，将影响后续管理器与实例之间的通讯",e);
            return null;
        }
        return client;
    }
}
