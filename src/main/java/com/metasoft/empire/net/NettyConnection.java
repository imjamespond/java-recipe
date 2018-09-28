package com.metasoft.empire.net;

import java.util.ArrayDeque;
import java.util.Deque;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.empire.utils.JsonLite;

/**
 * @author james
 *
 */
public class NettyConnection extends BaseConnection<Channel> {
	private static final Logger logger = LoggerFactory.getLogger(NettyConnection.class);

	// private static final long MAX_PKT_ACC_BYTES = 1048576L;
	// private static final long FINE_PKT_ACC_BYTES = 10240L;

	private String ipAddress;
	private long timeMark;
	private Deque<Object> msgQueue = new ArrayDeque<Object>();
	private int sent = 0;//only allow sent once

	public NettyConnection(Channel channel, String uuid) {
		
		this.channel = channel;
		this.uuid = uuid;
		this.ipAddress = this.channel.remoteAddress().toString();
	}

	public Channel getBindChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public long getTimeMark() {
		return timeMark;
	}

	public void markNow() {
		this.timeMark = System.currentTimeMillis();
	}

	public synchronized void close() {
		if (channel.isActive()) {
			channel.close();
		}
	}

	
	/* Request to write a message via this Channel through the ChannelPipeline. 
	 * This method will not request to actual flush, 
	 * so be sure to call flush() once you want to request to flush all pending data to the actual transport.
	 */
	@Override
	public ChannelFuture send(Object rsp) {
		if (channel.isActive()) {
			if (channel.isWritable()) {//
				return channel.write(rsp);
			} else {
				// Any write requests made when this method returns false are
				// queued until the I/O thread is ready to process the queued
				// write requests
				logger.warn("requests are queued");
				return channel.write(rsp);
			}
		} else {
			logger.warn("send rsp {} to unavailable session {}, rsp dropped", rsp, getUuid());
		}
		return null;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("NettyConn{Name=").append(getUuid()).append(", IP=").append(getIpAddress());
		return buf.toString();
	}

	public void deliverJson(Object message) {

		synchronized(this){
			if(sent > 0){
				//cached msg
				msgQueue.addLast(message);
			}else{
				JsonLite jl = new JsonLite(JsonLite.Type.Bracket);
				jl.appendValue((String) message);
				if(null==send(jl.convert2String())){
					//cached msg
					msgQueue.addLast(message);
				}
				sent = 1;
			}
		}
	}

	
	@Override
	public void deliver(Object message) {
		channel.writeAndFlush(new TextWebSocketFrame((String)message));
	}
	
	@Override
	public void deliverMsgCached() {
		JsonLite jl = new JsonLite(JsonLite.Type.Bracket);
		boolean cached = false;
		synchronized(this){
			while(msgQueue.size()>0){
				cached = true;
				Object obj = msgQueue.pop();
				if(obj instanceof String){
					String str = (String) obj;
					jl.appendValue(str);
					logger.warn("send cached msg {} ",str);
				}
			}
		}
		if(cached){
			send(jl.convert2String());
			sent = 1;
		}else{
			sent = 0;
		}
	}


	@Override
	public void sendAndClose(Object message) {
		if (this.channel.isActive()) {
			ChannelFuture cf = send(message);
			if (cf != null) {
				cf.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						close();
					}
				});
			}
		}
	}

}