package com.metasoft.flying.node.net;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.Service;

public abstract class RpcPeer {

    protected NewChannelCallback newChannelCallback;
    protected Map<String, Service> services = new ConcurrentHashMap<String, Service>();

    public void registerService(Service service) {
        services.put(service.getDescriptorForType().getName(), service);
    }

    public void setNewChannelCallback(NewChannelCallback newChannelCallback) {
        this.newChannelCallback = newChannelCallback;
    }

    protected void setupNewChannel(RpcChannel rpcChannel) {
        IoSession IoSession = rpcChannel.getChannel();
        RpcMessageHandler handler = (RpcMessageHandler) IoSession.getHandler();
        handler.setChannel(rpcChannel);
        rpcChannel.setServiceMap(Collections.unmodifiableMap(services));
        if (newChannelCallback != null) {
            newChannelCallback.run(rpcChannel);
        }
    }

    public abstract void channelConnected(IoSession IoSession);

    public abstract void channelDisconnected(IoSession IoSession);
}
