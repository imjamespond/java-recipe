package com.metasoft.flying.node.service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.ServiceException;
import com.metasoft.flying.node.EchoProto;
import com.metasoft.flying.node.EchoProto.EchoRequest;
import com.metasoft.flying.node.EchoProto.EchoResponse;
import com.metasoft.flying.node.EchoProto.EchoService;
import com.metasoft.flying.node.NodeProto;
import com.metasoft.flying.node.NodeProto.GenericResponse;
import com.metasoft.flying.node.NodeProto.NodeRequest;
import com.metasoft.flying.node.NodeProto.NodeService;
import com.metasoft.flying.node.NodeProto.IdentRequest;
import com.metasoft.flying.node.net.NewChannelCallback;
import com.metasoft.flying.node.net.RpcChannel;
import com.metasoft.flying.node.net.RpcClient;
import com.metasoft.flying.node.rpc.EchoServiceImpl;
import com.metasoft.flying.node.rpc.Red5ServiceImpl;

public class NodeClient {
	static final int COUNTDOWN = 100;
	static CountDownLatch latch = new CountDownLatch(COUNTDOWN);

	private static final Logger logger = LoggerFactory.getLogger(NodeClient.class);
	private static NodeClient nodeClient;
	private RpcClient client;
	private SocketAddress serverAddr;
	private NodeService.Stub remoteService;
	private NodeRequest nodeRequest;
	private NodeService.BlockingInterface remoteBlockService;
	private EchoService.Stub remoteEchoService;
	private EchoRequest echoRequest;

	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	private EchoServiceImpl echoService;
	private Red5ServiceImpl red5Service;
	private String host;
	private int port;
	private Red5ApplicationAdapter adapter;
	
	public void setAdapter(Red5ApplicationAdapter adapter) {
		this.adapter = adapter;
	}
	// private ScheduledFuture<?> reconnectFuture;
	public NodeClient() {
		client = new RpcClient();		
		nodeRequest = NodeRequest.newBuilder().setId("media-1").setType(0).build();
		echoRequest = EchoRequest.newBuilder().setPayload(".").build();
	}
	
	public static NodeClient getInstance(){
//		if(null == NodeClient.nodeClient){
//			nodeClient = new NodeClient();
//		}
		if( !NodeClient.nodeClient.client.isConnected()){
			return null;
		}
		return nodeClient;
	}
	
	public void init() {
		logger.debug("init");
		nodeClient = this;
		//注册rpc service
		client.registerService(EchoProto.EchoService.newReflectiveService(echoService ));
		client.registerService(NodeProto.Red5Service.newReflectiveService(red5Service ));
		reconnect();
	}

	public void connect() {
		serverAddr = new InetSocketAddress(host, port);
		final ConnectFuture future = client.startConnect(serverAddr, new NewChannelCallback() {
			@Override
			public void run(RpcChannel channel) {
				logger.debug("连接完成");
				//初始化远端调用
				remoteService = NodeService.newStub(channel);
				remoteBlockService = NodeService.newBlockingStub(channel);
				remoteEchoService = EchoService.newStub(channel);
				// for(int i=0; i<COUNTDOWN; i++)
				sendAsyncRegistRequest();
			}
		});

		future.addListener(new IoFutureListener<ConnectFuture>() {
			@Override
			public void operationComplete(ConnectFuture f) {
				if (f.getException() != null) {
					logger.debug("ConnectFuture Exception " + future.getException());
				} else {
					logger.debug("connect successfully");
					// reconnectFuture.cancel(true);
				}
			}
		});
	}
	
	public GenericResponse sendSyncIdentifyRequest(String uid, String key) {
		IdentRequest request = IdentRequest.newBuilder().setUid(uid).setLoginKey(key).build();
		try {
			return remoteBlockService.identifyConn(null, request);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendAsyncIdentifyRequest(final String uid, String key) {
		IdentRequest request = IdentRequest.newBuilder().setUid(uid).setLoginKey(key).build();
		RpcCallback<NodeProto.GenericResponse> done = new RpcCallback<NodeProto.GenericResponse>() {
			@Override
			public void run(GenericResponse response) {
				logger.debug("registRequest done");

				if(response==null){
					logger.debug("sendAsyncIdentifyRequest: NULL");
					adapter.forceClose(uid);
				}else if(response.getOk() == false){
					logger.debug("sendAsyncIdentifyRequest: FALSE");
					adapter.forceClose(uid);
				}
				logger.debug("sendSyncIdentifyRequest:OK");
			}
		};
		remoteService.identifyConn(null, request, done);
	}
	
	private void sendAsyncRegistRequest() {
		RpcCallback<NodeProto.GenericResponse> done = new RpcCallback<NodeProto.GenericResponse>() {
			@Override
			public void run(GenericResponse response) {
				System.out.println("registRequest done");
				// latch.countDown();
			}
		};
		remoteService.registNode(null, nodeRequest, done);
	}
	public void sendAsyncStreamStartRequest(String uid) {
		RpcCallback<NodeProto.GenericResponse> done = new RpcCallback<NodeProto.GenericResponse>() {
			@Override
			public void run(GenericResponse response) {
				logger.debug("registRequest done");
			}
		};
		IdentRequest request = IdentRequest.newBuilder().setUid(uid).setLoginKey("").build();
		remoteService.streamStart(null, request, done);
	}
	public void sendAsyncStreamStopRequest(String uid) {
		RpcCallback<NodeProto.GenericResponse> done = new RpcCallback<NodeProto.GenericResponse>() {
			@Override
			public void run(GenericResponse response) {
				logger.debug("registRequest done");
			}
		};
		IdentRequest request = IdentRequest.newBuilder().setUid(uid).setLoginKey("").build();
		remoteService.streamStop(null, request, done);
	}
	public void sendAsyncEchoRequest() {
		RpcCallback<EchoProto.EchoResponse> done = new RpcCallback<EchoProto.EchoResponse>() {
			@Override
			public void run(EchoResponse response) {
				System.out.println("echoRequest done");
			}
		};
		remoteEchoService.echo(null, echoRequest, done);
	}

	public void reconnect() {
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				logger.debug("client reconnecting");
				if (client!=null && client.isConnected()) {
					logger.debug("client isConnected");
					sendAsyncEchoRequest();
				} else {
					connect();
				}
			}
		}, 1000L, 60000L, TimeUnit.MILLISECONDS);
	}

	public void setEchoService(EchoServiceImpl echoService) {
		this.echoService = echoService;
	}

	public void setRed5Service(Red5ServiceImpl red5Service) {
		this.red5Service = red5Service;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		NodeClient.getInstance().reconnect();
		
		// latch.await();
		// System.exit(0);
	}
}
