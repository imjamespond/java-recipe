package com.pengpeng.stargame.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pengpeng.stargame.Broadcast;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.util.JsonUtil;
import com.pengpeng.stargame.vo.chat.ChatVO;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-8下午3:19
 */
@Component
public class FrontendServiceImpl implements IFrontendService{
    private static final Logger logger = Logger.getLogger(FrontendServiceImpl.class);

    @Autowired
    private ISessionContainer sessionContainer;

    private Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired
    private Broadcast broadcast;

    /**
     * 聊天信息广播
     * @param pids
     * @param json
     * @return
     */
    @Override
    public RpcResult broadcast(String[] pids, String json) {
        broadcast.broadcast(pids,json);
        return null;
    }

    /**
     * 各种事件的广播
     * @param pids
     * @param type
     * @param json
     * @return
     */
    @Override
    public RpcResult broadcast(String[] pids, String type, String json) {
       broadcast.broadcast(pids,type,json);
       return null;
    }


    /**
     * 修改指定用户的session信息
     * @param json
     */
    @Override
    public void onSessionEvent(String json) {
        Map<String, String> map = gson.fromJson(json,new TypeToken<Map<String,String>>(){}.getType());
        String pid = map.get("pid");
        Session session = sessionContainer.getElement(pid);
        if (session==null){
            logger.error("not found session:"+pid);
            return;
        }
        sessionContainer.updateSession(map);
    }

    @Override
    public void logout(Session session, String pid) {
        broadcast.reject(session);
    }
}
