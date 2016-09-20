package com.pengpeng.stargame.dispatcher;

import com.pengpeng.stargame.IChannel;
import com.pengpeng.stargame.IChannelContainer;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.rpc.Session;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午4:29
 */
@Component()
public class ChannelRpc extends StatusHandler {
    private static final Logger logger = Logger.getLogger(ChannelRpc.class);

    @Autowired
    private IChannelContainer container;

    public ChannelRpc(){

    }
    @RpcAnnotation(cmd="status.size",req= String.class,name="获取在线人数")
    public int size(Session session,String id){
        return  container.getChannel("chat.world").size();
    }

    @RpcAnnotation(cmd="channel.enter",req= String.class,name="进入频道")
    public void enterChannel(Session session,String id){
        IChannel channel = container.getChannel(id);
        channel.enter(session.getPid());
    }

    @RpcAnnotation(cmd="channel.outer",req= String.class,name="退出频道")
    public void outerChannel(Session session,String id){
        IChannel channel = container.getChannel(id);
        channel.outer(session.getPid());
    }

    @RpcAnnotation(cmd="channel.getMember",req= String.class,name="取得场景内所有的用户")
    public Session[] getMember(Session session,String id){
        Collection<Session> collections =  container.getMember(id);
        return collections.toArray(new Session[0]);
    }

    @RpcAnnotation(cmd="channel.random",req= String[].class,name="随机取得一个在线用户")
    public Session[] randomSession(Session session,String[] ids){
        Session[] s = container.random(ids);
        return s;
    }
    @RpcAnnotation(cmd="session.update",req= Map.class,name="更新session数据")
    public void updateSession(Session session,Map<String,String> map){
        if (map==null||map.isEmpty()){
            return ;
        }
        Session s = container.getSession(session.getPid());
        if(s==null){
            return ;
        }
        for(Map.Entry<String,String> entry:map.entrySet()){
            s.addParam(entry.getKey(),entry.getValue());
        }
    }

    @RpcAnnotation(cmd="session.clean",req= String.class,name="清理指定服务器的session")
    public void cleanSession(Session session,String sid){

    }

    @RpcAnnotation(cmd="chat.enter",req= String.class,name="进入聊天室")
    public void enterChat(Session session,String id){

    }

    @RpcAnnotation(cmd="chat.outer",req= String.class,name="退出聊天室")
    public void outerChat(Session session,String id){

    }
}
