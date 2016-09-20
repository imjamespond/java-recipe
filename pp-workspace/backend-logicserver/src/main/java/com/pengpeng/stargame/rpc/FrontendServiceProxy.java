package com.pengpeng.stargame.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.ServerType;
import com.pengpeng.stargame.managed.SimpleClientService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 前台服务器接口代理对象
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-15下午4:09
 */
//@Component()//使用配置文件完成
public class FrontendServiceProxy extends SimpleClientService {
    private static final Logger logger = Logger.getLogger(FrontendServiceProxy.class);
    private Map<String,RmiProxyFactoryBean> services = new HashMap<String, RmiProxyFactoryBean>();

    @Autowired
    private IManageService manageService;

    @Autowired
    private StatusRemote statusRemote;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 创建并保持前台服务器链接
     * @param id
     * @param url
     * @throws Exception
     */
    public void addService(String id,String url) throws Exception{
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceInterface(IFrontendService.class);
        bean.setServiceUrl(url);
        bean.setRefreshStubOnConnectFailure(true);
        bean.setCacheStub(true);
        bean.afterPropertiesSet();
        services.put(id,bean);
    }

    /**
     * 取得前台服务器的rmi接口服务对象
     * @param id
     * @return
     */
    private IFrontendService getService(String id){
        RmiProxyFactoryBean bean = services.get(id);
       //logger.info("Get Service: id = " + id);
        if (bean==null){
            NodeInfo node = manageService.getServerNodeById(id);
            if (node!=null){
                try {
                    String url = String.format("rmi://%s:%s/frontend",node.getHost(),node.getPort());
                    //logger.info("url:"+url);
                    addService(id,url);
                    bean = services.get(id);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        if (bean==null){
            return null;
        }
        IFrontendService service = (IFrontendService)bean.getObject();
        return service;
    }

    /**
     * 对用户列表,根据前台服务器id进行分类
     * @param sessions
     * @return
     */
    private Map<String,Set<String>> sort(Session[] sessions){
        Map<String,Set<String>> map = new HashMap<String,Set<String>>();
        for(Session s:sessions){
            if (null==s||null==s.getFrontend()){
                continue;
            }
            if (map.containsKey(s.getFrontend())){
                Set<String> sets = map.get(s.getFrontend());
                sets.add(s.getPid());
            }else{
                Set<String> sets = new HashSet<String>();
                sets.add(s.getPid());
                map.put(s.getFrontend(),sets);
            }
        }
        return map;
    }

    /**
     * 信息广播,一般只用于聊天,或文字性对话的广播内容
     * @param sessions
     * @param msg
     */
    public void broadcast(Session[] sessions, String msg) {
        if (sessions==null){
            return ;
        }
        Map<String,Set<String>> map = sort(sessions);
        for(Map.Entry<String,Set<String>> item:map.entrySet()){
            String[] list = item.getValue().toArray(new String[0]);
            getService(item.getKey()).broadcast(list, msg);
        }
    }

    public void broadcast(Session session, Object object) {
        broadcast(new Session[]{session},object);
    }

    /**
     * 对多个会话的广播
     * 1.找到整理并根据前台服务器id进行归类
     * 2.归类后,根据前台服务器id遍历,前台,对会话列表进行广播
     * @param sessions
     * @param object
     */
    public void broadcast(Session[] sessions, Object object) {
        if (sessions==null){
            return;
        }
        EventAnnotation anno = object.getClass().getAnnotation(EventAnnotation.class) ;
        if(anno==null){
            logger.error("not found EventAnnoation:"+object);
            return;
        }
        Map<String,Set<String>> map = sort(sessions);
        String type = anno.name();
        String json = gson.toJson(object);
        //logger.debug(" broadcast data:"+json);
        for(Map.Entry<String,Set<String>> item:map.entrySet()){
            String[] list = item.getValue().toArray(new String[0]);
            IFrontendService service = getService(item.getKey());
            if (service!=null){
                service.broadcast(list,type,json);
            }
        }
    }

    /**
     * 同步session数据
     * 1.首先更新status服务器的session信息
     * 2.更新前台服务器的session信息
     * @param s
     * @param map
     */
    public void onSessionEvent(Session s,Map<String, String> map) {
        statusRemote.updateSession(s,map);
        IFrontendService service = getService(s.getFrontend());
        if(service!=null){
        Map<String,String> value = new HashMap<String,String>();
        value.putAll(map);
        service.onSessionEvent(gson.toJson(value));
        }
    }

    /**
     * 退出游戏
     * 1.退出statusserver,并移出session信息
     * 2.退出gameserver,并溢出session信息
     * @param session
     * @param channelId
     */
    public void logout(Session session,String channelId) {
        if (session==null){
            logger.error("session is null:"+session);
            return ;
        }
        statusRemote.logout(session,channelId);
        IFrontendService service = this.getService(session.getFrontend());
        if (service==null){
            logger.error("not found frontend:"+session.getFrontend());
            return;
        }
        service.logout(session,session.getPid());
    }

    /**
     * 服务器停止服务的通知
     * 由主控服务器发起的事件通知
     * @param node
     */
    @Override
    public void notifyNodeStop(NodeInfo node) {
        if (ServerType.GAMESERVER.equalsIgnoreCase(node.getType())){
            //如果是前台服务器,挂掉
            logger.error(String.format("前台服务器挂掉:id=%s,host=%s,port=%s",node.getId(),node.getHost(),node.getPort()));
            services.remove(node.getId());
        }
        super.notifyNodeStop(node);
    }
}
