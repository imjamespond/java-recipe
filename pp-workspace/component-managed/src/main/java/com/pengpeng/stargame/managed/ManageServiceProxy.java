package com.pengpeng.stargame.managed;

import org.apache.log4j.Logger;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.util.List;

/**
 * 管理程序代理程序，实现了 IManageService ，封装了远程访问细节。
 *
 * @author ChenHonghong@gmail.com
 * @since 13-4-12 下午2:14
 */
public class ManageServiceProxy implements IManageService{

    private IManageService manageService;

    private static Logger logger = Logger.getLogger(ManageServiceProxy.class);

    private static boolean MANAGER_CONNECTED = false;

    public void init(){
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            final String serviceUrl = "rmi://" + NodeConfig.getManageUrl() + "/ManageService";
            proxy.setServiceUrl(serviceUrl);
            proxy.setServiceInterface(IManageService.class);
            proxy.afterPropertiesSet();
            proxy.setRefreshStubOnConnectFailure(true);
            manageService = (IManageService) proxy.getObject();
            MANAGER_CONNECTED = true;
            logger.info("成功连接上管理器的管理接口：" + serviceUrl);
        } catch (Exception e) {
            logger.warn("无法连接到服务器管理器（"+NodeConfig.getManageUrl()+"），稍后将自动重试");
        }
    }

    @Override
    public List<NodeInfo> getServerNodes() {
        try {
            return manageService.getServerNodes();
        } catch (Exception e) {
            logger.error("连接管理器失败，无法取得可用服务器列表");
            MANAGER_CONNECTED = false;
            return null;
        }
    }

    @Override
    public NodeInfo getServerNodeById(String serverId) {
        try {
            return manageService.getServerNodeById(serverId);
        } catch (Exception e) {
            logger.error("连接管理器失败，无法取得可用服务器列表");
            MANAGER_CONNECTED = false;
            return null;
        }
    }

    @Override
    public List<NodeInfo> getServerNodes(String serverType) {
        try {
            return manageService.getServerNodes(serverType);
        } catch (Exception e) {
            logger.error("连接管理器失败，无法取得可用服务器列表");
            MANAGER_CONNECTED = false;
            return null;
        }
    }

    @Override
    public NodeInfo getServerNode(String serverType) {
        try {
            if(manageService == null){
                logger.error("managerService 为 null，请检查配置");
                return null;
            }
            return manageService.getServerNode(serverType);
        } catch (Exception e) {
            logger.error("连接管理器失败，无法取得服务器实例信息："+ serverType,e);
            MANAGER_CONNECTED = false;
            return null;
        }
    }
}
