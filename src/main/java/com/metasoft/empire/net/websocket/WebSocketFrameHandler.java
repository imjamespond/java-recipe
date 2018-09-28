/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.metasoft.empire.net.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.metasoft.empire.net.BaseConnection;
import com.metasoft.empire.service.ConnectionService;
import com.metasoft.empire.service.RequestService;

/**
 * Echoes uppercase content of text frames.
 */
@Component
@Scope("prototype")
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	@Autowired
	private RequestService msgService;
	@Autowired
	private ConnectionService connectionService;
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
   
        if ((frame instanceof TextWebSocketFrame)) {
        	String request = ((TextWebSocketFrame) frame).text();
        	logger.debug("{}",request);
			processClientMessage(request, ctx);    	
		} else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            //throw new UnsupportedOperationException(message);
			logger.debug(message);
			ctx.channel().close();
		}
    }
    
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
    	//A {@link Channel} is active now, which means it is connected.
    	super.channelActive(ctx);  	

		connectionService.onCreateConnection(ctx.channel());
    }
    
    @Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception{
    	//A {@link Channel} is inactive now, which means it is closed.
    	super.channelInactive(ctx);
    	
		BaseConnection<?> conn = connectionService.getConnBySession(ctx.channel().hashCode());
		synchronized(conn){
		if (conn.authorized()) {
			logger.debug("connection disconnected:{}", conn.getSessionString());
			// remove session
			connectionService.onCloseConnection(conn);
		} else {
			logger.debug("unknown connection from disconnected:{}", conn.getSessionString());
		}
		}
    }
    /**/
	private void processClientMessage(String msg, ChannelHandlerContext ctx) {
		BaseConnection<?> conn = (BaseConnection<?>) connectionService.getConnBySession(ctx.channel().hashCode());
		if ((conn != null) && (conn.authorized())){
			msgService.receive(msg, conn);
		}else{
			logger.warn("connection is null or unauthorized:{}");
			ctx.channel().close();
		}
	}
}
