package com.pengpeng.stargame.dispatcher;

import com.pengpeng.stargame.IChannel;
import com.pengpeng.stargame.IChannelContainer;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-17上午10:33
 */
@Component()
public class StatusRpc extends StatusHandler {
    @Autowired
    private IChannelContainer container;

    @RpcAnnotation(cmd="status.login",req= String.class,name="登录")
    public void login(Session session,String channelId){
        container.login(session, channelId);
        String familyId = session.getParam(SessionUtil.KEY_CHANNEL_FAMILY);
        if (null!=familyId){
            IChannel channel = container.getChannel(familyId);
            channel.enter(session.getPid());
        }
        IChannel channel = container.getChannel("chat.world");
        channel.enter(session.getPid());
    }
    @RpcAnnotation(cmd="status.logout",req= String.class,name="退出")
    public void logout(Session session,String channelId){
        container.logout(session,channelId);
        String familyId = session.getParam(SessionUtil.KEY_CHANNEL_FAMILY);
        if (null!=familyId){
            IChannel channel = container.getChannel(familyId);
            channel.outer(session.getPid());
        }
        IChannel channel = container.getChannel("chat.world");
        channel.outer(session.getPid());
    }
    @RpcAnnotation(cmd="status.get.session",req= String.class,name="取得Session信息")
    public Session getSession(Session session,String pid){
        Session s = container.getSession(pid);
        return s;
    }

    @RpcAnnotation(cmd="status.get.somesession",req= String[].class,name="通过Id 取得多个Session信息")
    public Session [] getSomeSession(Session session,String [] pids){
        List<Session> list=new ArrayList<Session>();
        if(pids!=null&&pids.length>0){
            for(String pid:pids){
                Session sessionT=container.getSession(pid);
                if(sessionT!=null){
                    list.add(sessionT);
                }
            }
        }
        return list.toArray(new Session[0]);
    }
}
