package com.metasoft.flying.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class HeartbeatHandler extends IdleStateAwareChannelHandler {
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		super.channelIdle(ctx, e);
		// long deltaTime = System.currentTimeMillis()-
		// e.getLastActivityTimeMillis();
		logger.debug("last time " + e.getLastActivityTimeMillis());

		// if (deltaTime > 330000) {
		e.getChannel().close();
		logger.debug("channel timeout ");
		// }
	}
}
