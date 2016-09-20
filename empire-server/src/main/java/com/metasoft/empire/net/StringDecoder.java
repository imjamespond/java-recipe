package com.metasoft.empire.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.metasoft.empire.service.ConnectionService;
import com.metasoft.empire.service.HandlerService;
import com.metasoft.empire.service.RequestService;
import com.metasoft.empire.utils.HttpUriUtils;

@Component
@Scope("prototype")
public class StringDecoder extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(StringDecoder.class);
	@Autowired
	private RequestService msgService;
	@Autowired
	private HandlerService handlerService;
	@Autowired
	private ConnectionService connectionService;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("msg type: {}", msg.getClass());
		if (msg instanceof DefaultHttpRequest) {
			HttpRequest request = (DefaultHttpRequest) msg;
			HttpHeaders headers = request.headers();
			HttpMethod method = request.getMethod();
			String uriStr = request.getUri();
			for(Entry<String, String> entry :headers.entries()){
				if(entry.getKey()!=null && entry.getValue()!=null){
					logger.debug("header {}:{}", entry.getKey(), entry.getValue());
				}
			}
			logger.debug("method :{}",method.name());
			logger.debug("uriStr :{}",uriStr);
			
			processClientMessage(HttpUriUtils.parseUri(uriStr),ctx);

			
			//ctx.write("ok");
		}
		//If you prefer not to receive HttpContent in your handler,
		//place HttpObjectAggregator after HttpObjectDecoder in the ChannelPipeline.
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			logger.debug("content: {}",new String( content.content().array()));
			//processClientMessage((GeneralRequest) msg, ctx);
		}
		
		 ReferenceCountUtil.release(msg);
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//ctx.flush();
	}
	

//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//    	logger.debug("handlerAdded connected");
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//    	logger.debug("handlerRemoved connection close");
//    }	
	
	private void processClientMessage(Map<String, String> map, ChannelHandlerContext ctx) {
		String uuid = map.get("uuid");
		String token = map.get("token");
		//FIXME some token verification
		if(null != token){
			//long-poll request
			if(token.indexOf("long-poll")==0){
				if(null != uuid){
					BaseConnection<?> conn = connectionService.getConnByUuid(uuid);
					if(null == conn){
						//return
						//ctx.channel().write("session-expired");//keep polling
					}else{
						//start polling
						connectionService.onUpdateConnection(conn,ctx.channel());
						//msgService.receive(new GeneralRequest(map), conn);
					}
				}else{
					BaseConnection<?> conn = connectionService.onCreateConnection(ctx.channel());
					//return
					ctx.channel().write(conn.getUuid());
				}
			}else{
				//return
				ctx.channel().write("undefine-token");
			}
		}else {
			//logger.warn("connection is invalid and will be closed...");
			//String rs = handlerService.handleSyncRequest(new GeneralRequest(map));
			//return
			//ctx.channel().write(rs);
		}
	}
	

}