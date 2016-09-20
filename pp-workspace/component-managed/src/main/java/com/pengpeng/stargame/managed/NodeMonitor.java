package com.pengpeng.stargame.managed;

import org.apache.log4j.Logger;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * 节点监控程序。
 * <p>主要功能：</p>
 * <ul>
 *     <li>程序启动时向服务器管理程序注册</li>
 *     <li>程序关闭时向服务器管理程序注销</li>
 *     <li>定时取得 jvm 和 os 信息，向服务器管理程序汇报（心跳）</li>
 * </ul>
 *
 * @author ChenHonghong@gmail.com
 * @since 13-5-16 下午4:32
 */
public class NodeMonitor {

    private IMonitorService monitorService;

    private static Logger logger = Logger.getLogger(NodeMonitor.class);

    private static boolean MANAGER_CONNECTED = false;

    private static String status;

    private boolean monitorStarted = false;

    public void start() {
        if(monitorStarted){
           logger.info("Node Monitor is started");
        }

        logger.info("Starting Node Monitor");
        status = NodeStatus.ONSERVICE.getCode();

        String manageUrl = NodeConfig.getManageUrl();

        if (manageUrl == null) {
            logger.error("Can't connecte to RMI server manager, check server manager");
        }

        logger.info("Registering server node.");
        NodeInfo nodeInfo = NodeConfig.getInfo();
        if (nodeInfo == null) {
            logger.error("Can't connecte to RMI server manager, check local config");
            return;
        }

        //与远程服务器管理器关联
        logger.info("Connecting to RMI server manager:" + manageUrl);
        register();

        monitorStarted = true;
    }

    private void register(){
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            final String serviceUrl = "rmi://" + NodeConfig.getManageUrl() + "/MonitorService";
            proxy.setServiceUrl(serviceUrl);
            proxy.setServiceInterface(IMonitorService.class);
            proxy.afterPropertiesSet();
            proxy.setRefreshStubOnConnectFailure(true);
            monitorService = (IMonitorService) proxy.getObject();
            monitorService.register(NodeConfig.getInfo());
            MANAGER_CONNECTED = true;
            logger.info("成功连接上管理器的监控接口：" + serviceUrl);
            logger.info("Server node: " + NodeConfig.getHost() + ":" + NodeConfig.getPort() + " registered");
        } catch (Exception e) {
            logger.warn("无法连接到服务器管理器（"+NodeConfig.getManageUrl()+"），稍后将自动重试");
        }
    }

    public void onState() {

        if(!MANAGER_CONNECTED){
            register();
        }

        //manager 自已不需要发送状态数据
        /*if (ServerType.MANAGER.equals(NodeConfig.getType())) {
            return;
        }*/

/*        if (logger.isDebugEnabled()) {
            logger.debug("发送实例运行数据");
        }*/

        NodeInfo nodeInfo = NodeConfig.getInfo();
        if (nodeInfo == null) {
            logger.error("无法发送节点运行时数据，请检查配置");
            return;
        }

        updateRuntimeInfo();

        try {
            monitorService.onState(nodeInfo, NodeStatus.ONSERVICE.getCode());
        } catch (Exception e) {
            logger.error("无法连接管理器，发送状态失败");
            MANAGER_CONNECTED = false;
        }
    }

    private void updateRuntimeInfo(){
        NodeInfo nodeInfo = NodeConfig.getInfo();

        //计算内存信息
        Runtime r = Runtime.getRuntime();
        long freeMemory = r.freeMemory();
        long totalMemory = r.totalMemory();
        int unit = 1000 * 1000;
        Long memoryUsed = (totalMemory-freeMemory) /unit;
        Long memoryTotal = r.maxMemory() / unit;

        nodeInfo.getRuntime().setMemoryUsed(memoryUsed.intValue());
        nodeInfo.getRuntime().setMemoryTotal(memoryTotal.intValue());
    }

    private void register(NodeInfo nodeInfo) {
        register();
    }


    private void unRegister(NodeInfo nodeInfo) {

    }


    private void onState(NodeInfo nodeInfo, String status) {
        onState();
    }

    public static String getStatus(){
        return status;
    }

    /**
     * 修改本前的运行状态。
     * @param newStatus
     */
    public void setStatus(String newStatus){
        status = newStatus;
    }

    /**
     * 检查当前节点的运行状态是否正常。
     * @return true 的话，表示正常
     */
    public static boolean isOnService(){
        if(NodeStatus.ONSERVICE.getCode().equalsIgnoreCase(status)){
            return true;
        }
        return false;
    }
}
