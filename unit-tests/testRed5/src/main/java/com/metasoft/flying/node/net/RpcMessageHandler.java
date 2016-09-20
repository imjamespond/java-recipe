package com.metasoft.flying.node.net;

import java.io.IOException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcMessageHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(RpcMessageHandler.class);

	private volatile RpcChannel channel;
	private RpcPeer rpcPeer;

	public RpcMessageHandler(RpcPeer peer) {
		this.rpcPeer = peer;
	}

	public RpcChannel getChannel() {
		return channel;
	}

	public void setChannel(RpcChannel ch) {
		channel = ch;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		assert session == channel.getChannel();
		channel.messageReceived(session, message);
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		//if (logger.isDebugEnabled())
			//logger.debug("Message send:" + message);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.err.println("channelConnected");
		rpcPeer.channelConnected(session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.err.println("channelDisonnected");
		rpcPeer.channelDisconnected(session);
	}

	@Override
    public void exceptionCaught(IoSession session, Throwable e) throws Exception {
        if (e.getCause() instanceof IOException) {
        	IOException ioException = (IOException) e.getCause();
        	logger.debug("IOException:"+ioException.getMessage());
        }
	}
}
