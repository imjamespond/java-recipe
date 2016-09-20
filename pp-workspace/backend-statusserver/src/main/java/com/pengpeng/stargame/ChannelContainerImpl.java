package com.pengpeng.stargame;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.rpc.Session;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午3:26
 */
@Component()
public class ChannelContainerImpl extends HashMapContainer<String,IChannel> implements IChannelContainer {
    private static final Logger logger = Logger.getLogger(ChannelContainerImpl.class);
    //记录用户的登录状态
    private OnlineChannel onlineChannel = new OnlineChannel();
    private Lock lock = new ReentrantLock();
    @Override
    public IChannel getChannel(String id) {
        if (id==null){
            logger.error("channel:"+id);
            return null;
        }
//        lock.lock();
        IChannel channel =  getElement(id);
//        try{
            if (channel!=null){
                return channel;
            }
            channel = new ChannelImpl(id);
        addElement(channel);
//        }finally{
//            lock.unlock();
//        }
        return channel;
    }

    @Override
    public void login(Session session,String channelId) {
        onlineChannel.enter(session);
    }

    @Override
    public void logout(Session session,String channelId) {
        //只需要退出在线,别的频道由别的地方负责
//        IChannel channel = this.getChannel(channelId);
//        if (channel!=null){
//            channel.outer(session.getPid());
//        }
        for(IChannel channel:values()){
            channel.outer(session.getPid());
        }
        onlineChannel.outer(session);
    }

    @Override
    public Session getSession(String pid) {
        return onlineChannel.getSession(pid);
    }

    @Override
    public Collection<Session> getMember(String channelId) {
        IChannel channel = getChannel(channelId);
        if (null==channel){
            logger.error("channel not found:"+channelId);
        }
        Collection<String> colls = channel.getMembers();
        List<Session> list = new ArrayList<Session>();
        for(String pid:colls){
            list.add(onlineChannel.getSession(pid));
        }
        return list;
    }

    @Override
    public void cleanByServerId(String id) {
        onlineChannel.cleanByServerId(id);
    }

    @Override
    public Session[] random(String[] ids) {
        return onlineChannel.random(ids);
    }

}
