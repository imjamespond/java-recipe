package com.metasoft.flying.net;

import java.io.IOException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.service.net.RequestService;
import com.metasoft.flying.vo.HeartbeatPackage;
import com.metasoft.flying.vo.general.GeneralRequest;

@Component
@Scope("prototype")
public class MessageHandler extends SimpleChannelHandler {
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
	@Autowired
	private RequestService msgService;
	@Autowired
	private ConnectionService connectionService;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Object obj = e.getMessage();

		if ((obj instanceof GeneralRequest)) {
			processClientMessage((GeneralRequest) obj, ctx);
		} else if (obj instanceof HeartbeatPackage) {
			logger.trace("heart beat handler:{}", ctx.getChannel().getId());
		} else {
			logger.debug("unknown msg type, disconnect");
			ctx.getChannel().close();
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// System.out.println("Invoked when a Channel is open, bound to a local address, and connected to a remote address.");
		logger.debug("connection connected, ip={}", ctx.getChannel().getRemoteAddress());

		BaseConnection conn = connectionService.onCreateConnection(ctx.getChannel());
		if(null!=conn){
			ctx.setAttachment(conn);
		}else{
			ctx.getChannel().close();
		}
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {		
		BaseConnection conn = (BaseConnection) ctx.getAttachment();
		synchronized(conn){
		if (conn.authorized()) {
			logger.debug("connection disconnected:{}", conn.getSessionId());
			// remove session
			connectionService.onCloseConnection(conn);
		} else {
			logger.debug("unknown connection from disconnected:{}", ctx.getChannel().getRemoteAddress());
		}
		}
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if((e.getCause() instanceof CorruptedFrameException)){
        	logger.debug("CorruptedFrameException");
        }else if (e.getCause() instanceof IOException) {
        	IOException ioException = (IOException) e.getCause();
        	logger.debug("IOException:{}", ioException.getMessage());
        }
    }
	private void processClientMessage(GeneralRequest msg, ChannelHandlerContext ctx) {
		BaseConnection conn = (BaseConnection) ctx.getAttachment();
		if ((conn != null) && (conn.authorized())){
			msgService.receive(msg, conn);
		}else{
			logger.warn("connection is null or unauthorized:{}", ctx.getChannel().getId());
			ctx.getChannel().close();
		}
	}
}
