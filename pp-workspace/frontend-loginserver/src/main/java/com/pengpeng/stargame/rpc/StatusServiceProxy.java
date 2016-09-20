package com.pengpeng.stargame.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.ServerType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午5:28
 */

public class StatusServiceProxy implements IStatusService{
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 如果RMI SERVICE为NULL ,那么重复检查次数
     */
    public static final int FIND_COUNT = 5;

    @Autowired
    private IManageService manageService;

    private IStatusService statusService;

    protected Gson gson  =new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public IStatusService getStatusService() throws Exception {
        if(statusService == null){
            int count = FIND_COUNT;
            for(int i=0;i<count;i++){
                connectStatusServer();
                if(statusService !=null){
                    break;
                }else{
                    //如果链接不成功,则等待10毫秒,再连
                    Thread.sleep(10);
                }
            }
        }
        if(statusService == null){
            throw new RuntimeException("RMI LogicService fail.");
        }
        return statusService;
    }

    /**
     * init logicService
     * @throws Exception
     */
    private void connectStatusServer() throws Exception{
        NodeInfo node  = manageService.getServerNode(ServerType.STATUS);
        if(node == null){
            throw new NullPointerException("get Status case fail.");
        }
        statusService = this.regService(IStatusService.class,node,"status");
    }

    private <T> T regService(Class<T> T,NodeInfo node,String alias) throws Exception{
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            String url = String.format("rmi://%s:%d/%s",node.getHost(),node.getPort(),alias);
            proxy.setServiceUrl(url);
            proxy.setServiceInterface(T);
            proxy.setRefreshStubOnConnectFailure(true);
            proxy.afterPropertiesSet();
            proxy.setCacheStub(true);
            T service = (T) proxy.getObject();
            logger.info(String.format("Server node: %s:%s - alias:%s" ,node.getHost(), node.getPort(),alias));
            return service;
        } catch (Exception e) {
            logger.warn(String.format("RMI error>>> %s:%s - alias:%s",node.getHost(), node.getPort(), alias));
        }
        return null;
    }


    public <T> T execute(String code,Session session,String json,Class<T> cls){
        RpcResult result = null;
        try {
            result = process(code, session,json);
            if (result==null||result.getJson()==null){
                return null;
            }
            return gson.fromJson(result.getJson(),cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RpcResult process(String code, Session session, String json) throws Exception {
        IStatusService service = this.getStatusService();
        return service.process(code,session,json);
    }
}
