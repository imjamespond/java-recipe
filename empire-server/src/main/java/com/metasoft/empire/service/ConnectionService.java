package com.metasoft.empire.service;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.common.constant.ErrorCodes;
import com.metasoft.empire.net.BaseConnection;
import com.metasoft.empire.net.NettyConnection;

@Service
public class ConnectionService {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

	private final ConcurrentMap<Integer, BaseConnection<?>> sessions = new ConcurrentHashMap<Integer, BaseConnection<?>>();
	private final ConcurrentMap<String, Integer> uuids = new ConcurrentHashMap<String, Integer>();
	private final HashedWheelTimer timer = new HashedWheelTimer();
	//@Autowired
	//private UserService userService;

	//@Value("${max.conn.num}")
	private int maximumConn=9999;

	public BaseConnection<?> getConnBySession(Integer sid) {
		return (BaseConnection<?>) this.sessions.get(sid);
	}
	
	public BaseConnection<?> getConnByUuid(String uuid) {
		Integer sid = uuids.get(uuid);
		if(null!=sid)
			return (BaseConnection<?>) sessions.get(sid);
		else
			return null;
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

	public void banSessionId(Integer sid) {
		BaseConnection<?> client = getConnBySession(sid);
		if (client != null) {
			logger.debug("ADM:Ban user:{}", client.getUserId());
			banIpAddress(client.getIpAddress());
			forceDisconnect(client);
		}
	}

	public void broadcast(Object obj) {
		logger.trace("Broadcast message :{}",obj.toString());
		for (BaseConnection<?> conn : sessions.values())
			conn.deliver(obj);
	}

	/**
	 * 通过session发送
	 * 
	 * @param message
	 * @param sessionId
	 */
	public void deliver(String message, Integer sid) {
		BaseConnection<?> conn = getConnBySession(sid);
		if (conn != null) {
			logger.trace("Deliver message :{}",message);
			conn.deliver(message);
		} else {
			logger.warn("Client sid:{}, msg:{}",sid,message);
		}
	}

	public void forceDisconnect(Integer sid) {
		logger.debug("ADM:Disconnect sid={}", sid);
		BaseConnection<?> conn = getConnBySession(sid);
		forceDisconnect(conn);
	}

	private void forceDisconnect(BaseConnection<?> conn) {
		if (conn != null) {
			conn.close();
			sessions.remove(conn.getUuid());
		}
	}

	protected String getNextSessionUuid() {
		return UUID.randomUUID().toString();
	}

	public void onCloseConnection(BaseConnection<?> conn) {
		//logger.debug("on connection close, conn={}", conn.getUuid());
		
		synchronized(conn){
			conn.unAuthorize();
		}
		//Note that login request might still in other thread queue
		//if (null != conn.getUuid()) {
			//userService.onSignout(conn.getSessionId());
		sessions.remove(conn.getSession());
		uuids.remove(conn.getUuid());
		//}
	}

	public BaseConnection<?> onCreateConnection(Channel channel) {
		String uuid = getNextSessionUuid();
		if (sessions.size() < maximumConn) {
			final NettyConnection conn = new NettyConnection(channel, uuid);
			synchronized(conn){
				conn.passAuthorize();
				conn.markNow();
				conn.setSession(channel.hashCode());
			}
			
			sessions.put(conn.getSession(), conn);
			uuids.put(uuid, channel.hashCode());
			
			//heart beat detect
			timer.newTimeout(new TimerTask(){
				@Override
				public void run(Timeout timeout) throws Exception {
					synchronized(conn){
						if((System.currentTimeMillis()-conn.getTimeMark())>300000l){
							//TODO connection overtime after 5 minutes 
							conn.close();
						}else{
							timer.newTimeout(this, 600, TimeUnit.SECONDS);
						}						
					}
				}
			}, 600, TimeUnit.SECONDS);
			
			logger.debug("on connection create, conn={}", conn.getUuid());
			return conn;
		} else {
			//logger.warn("reach maximum connections: {}", sessions.size());
			return null;
		}
	}

	public void onUpdateConnection(BaseConnection<?> conn,Channel channel) {	
		NettyConnection netty = (NettyConnection) conn;
		synchronized(netty){
			//close current channel
			Channel preChannel = netty.getBindChannel();
			if(preChannel!=null&&preChannel.isActive()){
				preChannel.close();
			}
	
			netty.markNow();
			netty.setChannel(channel);
			netty.deliverMsgCached();
		}
		logger.debug("on connection update, uuid={}", conn.getUuid());
	}
	
	public int getConnectionNumber() {
		return sessions.size();
	}

	public void closing() {
		GeneralResponse vo = GeneralResponse.newError("SERVER_CLOSING", ErrorCodes.SERVER_CLOSING);
		for (Entry<Integer, BaseConnection<?>> entry : sessions.entrySet()) {
			BaseConnection<?> conn = entry.getValue();
			conn.send(vo);
		}
	}
}