package com.metasoft.flying.net;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PolicyServerHandler extends SimpleChannelHandler {

	private static final Logger logger = LoggerFactory.getLogger(PolicyServerHandler.class);

	private static final String SECURITY_FILE = "<cross-domain-policy>"
			+ "<site-control permitted-cross-domain-policies=\"all\"/>"
			+ "<allow-access-from domain=\"*\" to-ports=\"*\" />" + "</cross-domain-policy>\0";

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.debug("channelConnected");
		super.channelConnected(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.debug("channelDisconected");
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		String message = (String) e.getMessage();
		if (message.indexOf("<policy-file-request/>") != -1) {
			ctx.getChannel().write(SECURITY_FILE);
			String ip = ((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
			logger.debug("POLICY|" + ip);
		}
		// ctx.getChannel().close();
	}

	@Override
	public void disconnectRequested(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.debug("disconnectRequested");
		super.disconnectRequested(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		ctx.getChannel().close();
		logger.error("ERROR|SEND BOX " + e);
	}
}
