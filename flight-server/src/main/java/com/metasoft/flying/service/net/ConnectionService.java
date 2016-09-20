package com.metasoft.flying.service.net;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.constant.ErrorCodes;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.NettyConnection;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.vo.general.GeneralResponse;

@Service
public class ConnectionService {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

	private final ConcurrentMap<Integer, BaseConnection> sessions = new ConcurrentHashMap<Integer, BaseConnection>();

	@Autowired
	private UserService userService;

	@Value("${max.conn.num}")
	private int maximumConn;

	public BaseConnection getConnBySessionId(Integer sessionId) {
		return (BaseConnection) this.sessions.get(sessionId);
	}

	public void banIpAddress(String ip) {
		logger.debug("ADM:Ban client with ip={}", ip);
		// FIXME
		// add ip to map
		// conn.sendAndClose()
	}

	public void unbanIpAddress(String ip) {
		logger.debug("ADM:UnBan client with ip={}", ip);
		// remove ip
	}

	public void banSessionId(Integer sessionId) {
		BaseConnection client = getConnBySessionId(sessionId);
		if (client != null) {
			logger.debug("ADM:Ban user:{}", client.getUserId());
			banIpAddress(client.getIpAddress());
			forceDisconnect(client);
		}
	}

	public void broadcast(GeneralResponse message) {
		if (logger.isTraceEnabled()) {
			StringBuilder buf = new StringBuilder();
			buf.append("Broadcast message '");
			buf.append(message);
			logger.trace(buf.toString());
		}
		for (BaseConnection conn : this.sessions.values())
			conn.deliver(message);
	}

	/**
	 * 通过session发送
	 * 
	 * @param message
	 * @param sessionId
	 */
	public void deliver(GeneralResponse message, Integer sessionId) {
		BaseConnection conn = getConnBySessionId(sessionId);
		if (conn != null) {
			if (logger.isTraceEnabled()) {
				StringBuilder buf = new StringBuilder();
				buf.append("Deliver message '");
				buf.append(message);
				buf.append("' to client ");
				buf.append(conn);
				logger.trace(buf.toString());
			}
			conn.deliver(message);
		} else {
			StringBuilder buf = new StringBuilder();
			buf.append("Client sid=");
			buf.append(sessionId);
			buf.append(" not exist when deliver message ");
			buf.append(message);
			logger.warn(buf.toString());
		}
	}

	public void forceDisconnect(Integer sessionId) {
		logger.debug("ADM:Disconnect sid={}", sessionId);
		BaseConnection conn = getConnBySessionId(sessionId);
		forceDisconnect(conn);
	}

	private void forceDisconnect(BaseConnection conn) {
		if (conn != null) {
			conn.close();
			this.sessions.remove(conn.getSessionId());
		}
	}

	protected String getNextSessionId() {
		return UUID.randomUUID().toString();
	}

	public void onCloseConnection(BaseConnection conn) {
		logger.debug("on connection close, conn={}", conn.getSessionId());
		
		synchronized(conn){
			conn.unAuthorize();
		}
		//Note that login request might still in other thread queue
		if (null != conn.getSessionId()) {
			userService.onSignout(conn.getSessionId());
			this.sessions.remove(conn.getSessionId());
		}
	}

	public BaseConnection onCreateConnection(Channel channel) {
		// String sessionId = getNextSessionId();
		if (sessions.size() < maximumConn) {
			BaseConnection conn = new NettyConnection(channel, channel.getId());
			synchronized(conn){
				conn.passAuthorize();
			}
			sessions.put(channel.getId(), conn);
			logger.debug("on connection create, conn={}", conn.getSessionId());
			return conn;
		} else {
			//logger.warn("reach maximum connections: {}", sessions.size());
			return null;
		}
	}

	public int getConnectionNumber() {
		return sessions.size();
	}

	public void closing() {
		GeneralResponse vo = GeneralResponse.newError("SERVER_CLOSING", ErrorCodes.SERVER_CLOSING);
		for (Entry<Integer, BaseConnection> entry : sessions.entrySet()) {
			BaseConnection conn = entry.getValue();
			conn.send(vo);
		}
	}

	public void clean() {
		Iterator<Entry<Integer, BaseConnection>> it = sessions.entrySet().iterator();
		while ( it.hasNext() ) {
			Entry<Integer, BaseConnection> entry = it.next();
			BaseConnection conn = entry.getValue();
			if(!conn.isConnected()){
				it.remove();
			}
		}
	}
}