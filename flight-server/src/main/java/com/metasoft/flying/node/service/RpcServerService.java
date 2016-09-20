package com.metasoft.flying.node.service;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metasoft.flying.node.EchoProto;
import com.metasoft.flying.node.FlightProto;
import com.metasoft.flying.node.NodeProto;
import com.metasoft.flying.node.model.Node;
import com.metasoft.flying.node.net.RpcChannel;
import com.metasoft.flying.node.net.RpcPipelineFactory;
import com.metasoft.flying.node.net.RpcServer;
import com.metasoft.flying.node.rpc.EchoServiceImpl;
import com.metasoft.flying.node.rpc.NodeServiceImpl;
import com.metasoft.flying.node.rpc.PvpSearchServiceImpl;
import com.metasoft.flying.service.common.SpringService;

@Service
public class RpcServerService extends RpcServer {
	private static final Logger logger = LoggerFactory.getLogger(RpcServerService.class);
	
	@Autowired
	private RpcPipelineFactory rpcPipelineFactory;

	private ServerBootstrap bootstrap;
	private Channel nodeChannel;
	
	private ConcurrentMap<String, Node> nodeMap = new ConcurrentHashMap<String, Node>();//id->node
	private ConcurrentMap<Integer, String> sessionMap = new ConcurrentHashMap<Integer, String>();//session->node
	
    public RpcServerService() {

    }
    
    @PostConstruct
    public void init(){
        ChannelFactory channelFactory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool(),2);
        bootstrap = new ServerBootstrap(channelFactory);
        bootstrap.setPipelineFactory(rpcPipelineFactory);
        
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.connectTimeoutMillis", 100);
    }
    
    public void start(int port) {
    	registerService(EchoProto.EchoService.newReflectiveService(SpringService.getBean(EchoServiceImpl.class) ));
    	registerService(NodeProto.NodeService.newReflectiveService(SpringService.getBean(NodeServiceImpl.class) ));
    	registerService(FlightProto.PvpSearchService.newReflectiveService(SpringService.getBean(PvpSearchServiceImpl.class) ));
    	nodeChannel = bootstrap.bind(new InetSocketAddress(port));
    }
    
    public void shutdown() {    
    	ChannelFuture nodeChannelFuture = nodeChannel.close();
    	nodeChannelFuture.awaitUninterruptibly();
    	bootstrap.releaseExternalResources();
    }

    public Node getOnlineNodeById(String id){
    	return nodeMap.get(id);
    }
//    public Node getOnlineNodeBySession(Integer session){
//    	return sessionMap.get(session);
//    }
       
	public Node onSignIn(Node node) {
		// multi sign in
		Node dualNode = nodeMap.put(node.getId(), node);
		if (dualNode != null) {
			final RpcChannel preChannel = dualNode.getChannel();
			if (preChannel != null) {
				if (preChannel.getChannel().getId() != node.getChannel().getChannel().getId()) {//compare session
					logger.info("multi sign in|" + dualNode.getId() + "|" + preChannel.getChannel().getId());
					preChannel.getChannel().close();
				}
			}
		}
		//TODO update node data with what of dual
		node.setLoginTime(System.currentTimeMillis());
		sessionMap.put(node.getChannel().getChannel().getId(), node.getId());
		return node;
	}

	public void onSignout(int session) {
		String id = sessionMap.get(session);
    	if(null != id){
    		nodeMap.remove(id);
    	}
	}

	@Override
    public void channelConnected(Channel channel) {
        RpcChannel rpcChannel = new RpcChannel(channel);
        setupNewChannel(rpcChannel);
    }

    @Override
    public void channelDisconnected(Channel channel) {
    	onSignout(channel.getId());
    }
}
