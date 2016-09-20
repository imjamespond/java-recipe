package com.metasoft.flying.node.net;

import java.net.SocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient extends RpcPeer {
	private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
	static {
		logger.debug("RpcClient");
	}

	private NioSocketConnector connector;
    private volatile RpcChannel rpcChannel;

    public RpcClient() {
    	try {
			if (connector == null) {
				connector = new NioSocketConnector();
				connector.getFilterChain()
				.addLast("codec", new ProtocolCodecFilter(new RpcEncoder(), new RpcDecoder()));

				connector.setHandler(new RpcMessageHandler(this));

				connector.setConnectTimeoutMillis(10000L);
				connector.setConnectTimeoutCheckInterval(10000L);
				connector.getSessionConfig().setMinReadBufferSize(512);
				connector.getSessionConfig().setMaxReadBufferSize(16384);
				connector.getSessionConfig().setSendBufferSize(16384);
				connector.getSessionConfig().setReceiveBufferSize(16384);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public ConnectFuture startConnect(SocketAddress addr, NewChannelCallback newChannelCallback) {
		final ConnectFuture future = this.connector.connect(addr);
		this.newChannelCallback = newChannelCallback;
		return future;
    }

    public void stop() {
    	connector.dispose();
    }
    
    public boolean isConnected(){
    	return connector.isActive();
    }

    @Override
    public void channelConnected(IoSession IoSession) {
        //if (rpcChannel == null) {
            rpcChannel = new RpcChannel(IoSession);
            setupNewChannel(rpcChannel);
        //}
    }

    @Override
    public void channelDisconnected(IoSession IoSession) {
        assert IoSession == rpcChannel.getChannel();
        rpcChannel.disconnected();
    }

    public RpcChannel getChannel() {
        return rpcChannel;
    }

}
