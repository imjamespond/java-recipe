package com.metasoft.flying.net;

import org.jboss.netty.channel.ChannelFuture;

public abstract class BaseConnection {
	protected Integer sessionId;
	protected long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	private State currentState = State.Unauthorized;

	public boolean authorized() {
		return this.currentState == State.authorized;
	}

	public boolean suspend() {
		return this.currentState == State.Unauthorized;
	}

	public boolean queueFull() {
		return this.currentState == State.QueueFull;
	}

	public void passAuthorize() {
		this.currentState = State.authorized;
	}

	public void unAuthorize() {
		this.currentState = State.Unauthorized;
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public abstract String getIpAddress();

	public abstract ChannelFuture send(Object response);

	public abstract void close();
	
	public abstract boolean isConnected();

	public abstract void deliver(Object response);

	public abstract void sendAndClose(Object response);

	static enum State {
		Unauthorized, authorized, QueueFull;
	}

}