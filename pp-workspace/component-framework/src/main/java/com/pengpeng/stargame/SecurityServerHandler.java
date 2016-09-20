package com.pengpeng.stargame;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 */
@Component
public class SecurityServerHandler extends SimpleChannelHandler {

    private final Logger log = Logger.getLogger(this.getClass());

    private static final String SECURITY_FILE =
            "<cross-domain-policy>" +
                    "<site-control permitted-cross-domain-policies=\"all\"/>" +
                    "<allow-access-from domain=\"*\" to-ports=\"*\" />" +
                    "</cross-domain-policy>\0";

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        log.debug("channelConnected");
        super.channelConnected(ctx, e);
    }


    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        log.debug("channelDisconected");
        super.channelDisconnected(ctx, e);
    }
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String message = (String) e.getMessage();
        log.info(message);
        if (message.indexOf("<policy-file-request/>") != -1) {
            ctx.getChannel().write(SECURITY_FILE);
            String ip = ((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
            log.info("POLICY|"+ip);
        }
        //ctx.getChannel().close();
    }
    @Override
    public void disconnectRequested(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        log.debug("disconnectRequested");
        super.disconnectRequested(ctx, e);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        ctx.getChannel().close();
        log.error("ERROR|SEND BOX " + e);
    }
}
