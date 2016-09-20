package com.metasoft.flying.net;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;

public class NettyConnection extends BaseConnection {
	private static final Logger logger = LoggerFactory.getLogger(NettyConnection.class);

	// private static final long MAX_PKT_ACC_BYTES = 1048576L;
	// private static final long FINE_PKT_ACC_BYTES = 10240L;

	private Channel channel;
	private String ipAddress;

	public NettyConnection(Channel channel, Integer sessionId) {
		this.channel = channel;
		this.sessionId = sessionId;
		this.ipAddress = this.channel.getRemoteAddress().toString();
	}

	public Channel getBindChannel() {
		return this.channel;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public synchronized void close() {
		if (channel.isConnected()) {
			channel.close();
		}
	}
	
	public boolean isConnected(){
		return channel.isConnected();
	}

	@Override
	public ChannelFuture send(Object rsp) {
		if (this.channel.isConnected()) {
			if (getBindChannel().isWritable()) {//
				return channel.write(rsp);
			} else {
				// Any write requests made when this method returns false are
				// queued until the I/O thread is ready to process the queued
				// write requests
				StringBuilder buf = new StringBuilder();
				buf.append("requests are queued ");
				buf.append(getSessionId());
				logger.warn(buf.toString());
				return channel.write(rsp);
			}
		} else {
			//if conn is disconnected, remove it
			UserService us = SpringService.getBean(UserService.class);
			us.removeUser(userId);
			logger.warn("send rsp {} to unavailable session {}, rsp dropped", rsp, getSessionId());
		}
		return null;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("NettyConn{Name=").append(getSessionId()).append(", IP=").append(getIpAddress());
		return buf.toString();
	}

	@Override
	public void deliver(Object message) {
		send(message);
	}

	@Override
	public void sendAndClose(Object message) {
		if (this.channel.isConnected()) {
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