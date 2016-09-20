package com.pengpeng.stargame;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.rpc.Session;

import java.util.Collection;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午3:15
 */
public interface IChannelContainer extends IMapContainer<String,IChannel> {

    public IChannel getChannel(String id);

//    public IChannel getOnlineChannel();

    public void login(Session session,String channelId);

    public void logout(Session session,String channelId);

    public Session getSession(String pid);

    public Collection<Session> getMember(String channelId);

    void cleanByServerId(String id);

    public Session[] random(String[] ids);
}
