package com.pengpeng.test;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-8上午11:52
 */
public class RemoteRquest  {

    private boolean refreshConectFailure = true;
//    private Map<String,IBackendService> requests = new HashMap<String,IBackendService>();

//    public void removeProxy(IBackendService request){
//
//    }
    public void setRefreshConectFailure(boolean refreshConectFailure) {
        this.refreshConectFailure = refreshConectFailure;
    }

//    public IBackendService addProxy(String url){
//        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
//        proxy.setServiceInterface(IBackendService.class);
//        proxy.setRefreshStubOnConnectFailure(refreshConectFailure);
//        proxy.setServiceUrl(url);
//        proxy.afterPropertiesSet();
//        return (IBackendService)proxy.getObject();
//    }
//
//    @Override
//    public RpcResult process(String code, Session session, String json) throws Exception {
//        session.getUid();
//        return null;  //TODO:方法需要实现
//    }
//
//    public static void main(String[] args) throws Exception {
//        RmiServiceExporter exporter = new RmiServiceExporter();
//        exporter.setRegistryPort(1199);
//        exporter.setServiceName("request");
//        exporter.setServiceInterface(IBackendService.class);
//        exporter.setService(new IBackendService() {
//            @Override
//            public RpcResult process(String code, final Session session, String json) throws Exception {
//                System.out.println("调用成功!"+session.getUid());
//                return null;  //TODO:方法需要实现
//            }
//        });
//        exporter.afterPropertiesSet();
//
//        RemoteRquest proxy = new RemoteRquest();
//        IBackendService p = proxy.addProxy("rmi://192.168.10.124:1199/request");
//        p.process(null,new Session("uid","fid"),null);
//    }
}
