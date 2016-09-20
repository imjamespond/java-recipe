package com.pengpeng.stargame.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.NodeStatus;
import com.pengpeng.stargame.managed.ServerType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午5:28
 */
@Component
public class BackendServiceProxy implements IBackendService{
    private final Logger logger = Logger.getLogger(getClass());

    /**
     * 如果RMI SERVICE为NULL ,那么重复检查次数
     */
    public static final int FIND_COUNT = 5;

    @Autowired
    private IManageService manageService;

    private IBackendService logicService;

    private String url;
    protected Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //    @PostConstruct
    protected void init(){
        try {
            getLogicService();
        } catch (Exception e) {
            logicService = null;
            logger.error(e);
        }
    }
    public IBackendService getLogicService() throws Exception {
        if (logicService==null){
            connectLogicServer();
        }

        if(logicService == null){
            throw new RuntimeException("RMI LogicService fail.");
        }

        if (logicService!=null){
            try{
                String state = logicService.testConnecting();
                if (!NodeStatus.ONSERVICE.getCode().equals(state)){
                    logicService=null;
                    connectLogicServer();
                }
            }catch(Exception e){
                logicService = null;
                connectLogicServer();
                logger.error(e);
            }
        }
        return logicService;
    }

    /**
     * init logicService
     * @throws Exception
     */
    private void connectLogicServer() throws Exception{
        NodeInfo logicNode  = manageService.getServerNode(ServerType.LOGIC);
        if(logicNode == null){
            throw new NullPointerException("get Logic case fail.");
        }
        regService(logicNode,"logic");
    }

    private void regService(NodeInfo node,String alias){
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            url = String.format("rmi://%s:%d/%s",node.getHost(),node.getPort(),alias);
            proxy.setServiceUrl(url);
            proxy.setServiceInterface(IBackendService.class);
            proxy.setRefreshStubOnConnectFailure(true);
            proxy.setCacheStub(true);
            proxy.afterPropertiesSet();
            proxy.prepare();
            logicService = (IBackendService)proxy.getObject();
            logger.info("Server node: " + node.getHost() + ":" + node.getPort() + " - alias:"+alias);
        } catch (Exception e) {
            logger.error(e);
            logger.warn("RMI error>>> " + node.getHost() + ":" + node.getPort() + " - alias:" + alias);
        }
    }

    public <T> T execute(String code,Session session,String json,Class<T> cls) throws GameException {
        logger.debug("connect to server:"+url);
        RpcResult result = null;
        try {
            result = process(code, session,json);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        if (result == null){
            throw new GameException("服务器正在维护!");
        }
        if (!result.isOk()){
            throw new GameException(result.getMsg());
        }
        if (result.getJson()==null){
            return null;
        }
        return gson.fromJson(result.getJson(),cls);
    }

    @Override
    public String testConnecting() {
        IBackendService service = null;
        try {
            service = getLogicService();
            String state = service.testConnecting();
            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RpcResult process(String code, Session session, String json) throws Exception {
        IBackendService service = this.getLogicService();
        return service.process(code,session,json);
    }}
