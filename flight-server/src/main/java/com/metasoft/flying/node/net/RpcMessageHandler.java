package com.metasoft.flying.node.net;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RpcMessageHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = LoggerFactory.getLogger(RpcMessageHandler.class);

    private volatile RpcChannel channel;
    @Autowired
    private RpcServer rpcServer;

    public RpcMessageHandler() {
    	logger.debug("new RpcMessageHandler");
    }

    public RpcChannel getChannel() {
        return channel;
    }

    public void setChannel(RpcChannel ch) {
        channel = ch;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    	logger.debug("channelConnected");
        rpcServer.channelConnected(e.getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    	logger.debug("channelDisonnected");
        rpcServer.channelDisconnected(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        assert e.getChannel() == channel.getChannel();
        channel.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if((e.getCause() instanceof CorruptedFrameException)){
        	logger.debug("CorruptedFrameException");
        }else if (e.getCause() instanceof Exception) {
        	Exception ioException = (Exception) e.getCause();
			StringWriter errors = new StringWriter();
			ioException.printStackTrace(new PrintWriter(errors));
			logger.error("exception, trace:\n{}",  errors.toString()); 
        }
    }
}
