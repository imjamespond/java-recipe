package com.metasoft.flying.net.client;

import java.io.IOException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.net.MessageDecoder;
import com.metasoft.flying.net.MessageEncoder;

public class NettyChannelPipelineFactory implements ChannelPipelineFactory {
	private static final Logger logger = LoggerFactory.getLogger(NettyChannelPipelineFactory.class);
	private NettyClient client;
	
	public NettyChannelPipelineFactory(NettyClient client){
		this.client = client;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		p.addLast("decoder", new MessageDecoder());
		p.addLast("encoder", new MessageEncoder());
		p.addLast("handler", new SimpleChannelUpstreamHandler() {
			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
				//logger.debug("channelConnected");
			}

			@Override
			public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
				logger.debug("channelDisonnected");
				// rpcPeer.channelDisconnected(e.getChannel());
			}

			@Override
			public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
				//logger.debug("messageReceived");
				//Channel c = e.getChannel();
				Object obj = e.getMessage();
				client.onMessageRecv(obj);
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
				if ((e.getCause() instanceof CorruptedFrameException)) {
					logger.debug("CorruptedFrameException");
				} else if (e.getCause() instanceof IOException) {
					IOException ioException = (IOException) e.getCause();
					logger.debug("IOException:" + ioException.getMessage());
				}
			}
		});
		return p;
	}

}