package com.metasoft.flying.node.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.red5.server.BaseConnection;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IBroadcastScope;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IClientBroadcastStream;
import org.red5.server.api.stream.IStreamCapableConnection;
import org.red5.server.api.stream.IStreamService;
import org.red5.server.net.rtmp.status.Status;
import org.red5.server.net.rtmp.status.StatusCodes;
import org.red5.server.stream.StreamService;
import org.red5.server.util.ScopeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * 
 *         打开频道 org.red5.server.stream.StreamService->publish(String name,
 *         String mode)
 *         org.red5.server.adapter.MultiThreadedApplicationAdapter->
 *         streamPublishStart
 * 
 *         频道已经占用自动关闭 org.red5.server.stream.StreamService IBroadcastScope
 *         bsScope = getBroadcastScope(scope, name); if (bsScope != null &&
 *         !bsScope.getProviders().isEmpty()) { // another stream with that name
 *         is already published sendNSFailed(streamConn,
 *         StatusCodes.NS_PUBLISH_BADNAME, name, name, streamId); return; }
 *         org.red5.server.stream.StreamService->closeStream
 *
 *         注销频道 org.red5.server.stream.StreamService->closeStream
 *         org.red5.server.stream.ProviderService->unregisterBroadcastStream
 */

public class Red5ApplicationAdapter extends ApplicationAdapter {
	private static final Logger logger = LoggerFactory.getLogger(Red5ApplicationAdapter.class);
	private static final String USERID = "userId";
	private ConcurrentMap<String, IConnection> userMap = new ConcurrentHashMap<String, IConnection>();
	private ConcurrentMap<String, StreamUser> streamMap = new ConcurrentHashMap<String, StreamUser>();
	private NodeClient client;
	
	
	public NodeClient getClient() {
		return client;
	}

	public void setClient(NodeClient client) {
		this.client = client;
	}

	@Override
	public synchronized boolean connect(IConnection conn, IScope scope, Object[] params) {
		logger.debug(String.format("connect:%s", scope.getName()));

		// 用户首次连接server 时触发
		if (!super.connect(conn, scope, params)) {
			//return false;
		}

		// 获取用户名
		if (params != null && params.length > 0) {
			String userId = params[0].toString().trim();
			conn.setAttribute(USERID, userId);
			
			if(client==null){
				logger.debug("NodeClient NULL");
				//return false;
			}
			
			client.sendAsyncEchoRequest();
			
//			GenericResponse gr = client.sendSyncIdentifyRequest(userId, userId);
//			if(null==gr){
//				logger.debug("sendSyncIdentifyRequest NULL");
//				return false;
//			}else if(gr.getOk() == false){
//				logger.debug("sendSyncIdentifyRequest:FALSE");
//				return false;
//			}
//			logger.debug("sendSyncIdentifyRequest:OK");
			userMap.put(userId, conn);
		} else {
			return false;
		}

		return true;
	}

	@Override
	public synchronized void disconnect(IConnection conn, IScope scope) {
		Object obj = conn.getAttribute(USERID);
		if (obj != null) {
			String userId = (String) obj;
			userMap.remove(userId);
		}
	}

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		logger.debug("streamBroadcastStart");
		if (stream instanceof IClientBroadcastStream) {
			String streamName = stream.getPublishedName();
			StreamUser sUser = streamMap.get(streamName);
			if (null == sUser) {
				sUser = new StreamUser();
				streamMap.put(streamName, sUser);
			}

			sUser.stream = (IClientBroadcastStream) stream;
			sUser.conn = Red5.getConnectionLocal();
		}
		
		if(NodeClient.getInstance()!=null){
			//logger.debug("NodeClient NULL");
			IConnection conn = Red5.getConnectionLocal();
			Object obj = conn.getAttribute(USERID);
			if (obj != null) {
				String userId = (String) obj;
				if(client!=null){
					client.sendAsyncStreamStartRequest(userId);
				}
			}
		}
		
	}
	
	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		logger.debug("streamBroadcastClose");
		if(NodeClient.getInstance()!=null){
			//logger.debug("NodeClient NULL");
			IConnection conn = Red5.getConnectionLocal();
			Object obj = conn.getAttribute(USERID);
			if (obj != null) {
				String userId = (String) obj;
				if(client!=null){
					client.sendAsyncStreamStopRequest(userId);
				}
			}
		}
	}


	@Override
	public synchronized void stop(IScope scope) {
		logger.debug("stop");
		super.stop(scope);
	}

	public void streamClose(String userId) {

		StreamUser sUser = streamMap.get(userId);
		if (null != sUser) {

			if (null != sUser.conn) {
				IConnection conn = sUser.conn;
				if (conn instanceof IStreamCapableConnection) {
					IStreamCapableConnection scConn = (IStreamCapableConnection) conn;

					if (sUser.stream != null) {
						IClientBroadcastStream stream = sUser.stream;
						// this is a broadcasting stream (from Flash Player to
						// Red5)
						IStreamService streamService = (IStreamService) ScopeUtils.getScopeService(conn.getScope(),
								IStreamService.class, StreamService.class);
						IBroadcastScope bsScope = ((StreamService) streamService).getBroadcastScope(conn.getScope(),
								stream.getPublishedName());
						if (bsScope != null && conn instanceof BaseConnection) {
							((BaseConnection) conn).unregisterBasicScope(bsScope);
						}

						scConn.deleteStreamById(stream.getStreamId());
						// in case of broadcasting stream, status is sent
						// automatically by Red5
						if (!(stream instanceof IClientBroadcastStream)) {
							StreamService.sendNetStreamStatus(conn, StatusCodes.NS_PLAY_STOP,
									"Stream closed by server", stream.getName(), Status.STATUS, stream.getStreamId());
						}
					} else {
						log.info("Stream not found: streamId={}, connection={}", userId, conn);
					}
				} else {
					log.warn("Connection is not instance of IStreamCapableConnection: {}", conn);
				}
			}

			if (sUser.stream != null) {
				sUser.stream.close();
			}
		}
	}

	public void forceClose(String userId) {
		IConnection conn = userMap.remove(userId);
		if (null != conn) {
			conn.close();
		}
	}

	class StreamUser {
		public IClientBroadcastStream stream;
		public IConnection conn;
	}
	
}
