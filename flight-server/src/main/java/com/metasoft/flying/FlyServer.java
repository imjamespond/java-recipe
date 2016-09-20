package com.metasoft.flying;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.metasoft.flying.net.HeartbeatHandler;
import com.metasoft.flying.net.MessageDecoder;
import com.metasoft.flying.net.MessageEncoder;
import com.metasoft.flying.net.MessageHandler;
import com.metasoft.flying.net.PolicyServerHandler;
import com.metasoft.flying.node.service.RpcServerService;
import com.metasoft.flying.service.CleanService;
import com.metasoft.flying.service.FlightService;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.NpcService;
import com.metasoft.flying.service.PkArenaService;
import com.metasoft.flying.service.RankService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.common.JobService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.service.net.HandlerService;
import com.qianxun.service.ExchangeToFlyingService;
import com.qianxun.service.ExchangeToWebService;

public class FlyServer {
	private static final Logger logger = LoggerFactory.getLogger(FlyServer.class);
	private static ApplicationContext context;

	private ServerBootstrap serverBootstrap;
	private ServerBootstrap policyBootstrap;

	private Channel server;
	private Channel policy;
	
	private RpcServerService rpcServer;

	public static void main(String[] args) throws Exception {
		System.setProperty("spring.profiles.active", "home-dev" );//   "development"
		FlyServer server = new FlyServer();
		server.startup();
	}

	public void startup() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Properties properties = (Properties) context.getBean("properties");
		int policyPort = Integer.valueOf(properties.getProperty("policy.port"));
		int serverPort = Integer.valueOf(properties.getProperty("server.port"));
		int nodePort = Integer.valueOf(properties.getProperty("node.port"));
		
		//do some initialization
		HandlerService hs = context.getBean(HandlerService.class);
		hs.init();
		StaticDataService ss = context.getBean(StaticDataService.class);
		ss.init();
		CleanService cs = context.getBean(CleanService.class);
		cs.init();
		JobService js = context.getBean(JobService.class);
		js.init();
		
		MatchService ms = context.getBean(MatchService.class);
		ms.init();
		PkArenaService ps = context.getBean(PkArenaService.class);
		ps.init();
		RankService rs = context.getBean(RankService.class);
		rs.init();
		GameRoomService gs = context.getBean(GameRoomService.class);
		gs.init();
		FlightService fs = context.getBean(FlightService.class);
		fs.init();
		NpcService npc = context.getBean(NpcService.class);
		npc.init();
		
		ExchangeToFlyingService etfs = context.getBean(ExchangeToFlyingService.class);
		etfs.init();
		ExchangeToWebService etws = context.getBean(ExchangeToWebService.class);
		etws.init();
		
		// server
		ChannelFactory serverFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool(), 4);
		serverBootstrap = new ServerBootstrap(serverFactory);
		serverBootstrap.setPipelineFactory(getChannelPipelineFactory());
		serverBootstrap.setOption("child.tcpNoDelay", true);
		serverBootstrap.setOption("child.keepAlive", true);
		serverBootstrap.setOption("child.reuseAddress", true);
		serverBootstrap.setOption("child.connectTimeoutMillis", 100);
		serverBootstrap.setOption("readWriteFair", true);
		// serverBootstrap.setOption("child.bufferFactory",new
		// HeapChannelBufferFactory(ByteOrder.LITTLE_ENDIAN));
		server = serverBootstrap.bind(new InetSocketAddress(serverPort));
		logger.info("FlightServer started at port : " + serverPort);

		// flash policy server
		NioServerSocketChannelFactory policyFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), 2);
		policyBootstrap = new ServerBootstrap(policyFactory);
		policyBootstrap.setPipelineFactory(getPolicyPipelineFactory());
		policyBootstrap.setOption("child.tcpNoDelay", true);
		policyBootstrap.setOption("child.keepAlive", true);
		policyBootstrap.setOption("reuseAddress", true);
		policyBootstrap.setOption("child.connectTimeoutMillis", 100);
		logger.info("PolicyServer started at port : " + policyPort);
		policy = policyBootstrap.bind(new InetSocketAddress(policyPort));// 843 映射// 到7003

		// rpc server
		rpcServer = SpringService.getBean(RpcServerService.class);
		rpcServer.start(nodePort);
		logger.info("NodeServer started at port : " + nodePort);

	}

	public void shutDown() {
		logger.info("shutting down FlightServer...");
		ChannelFuture serverF = server.close();
		serverF.awaitUninterruptibly();
		serverBootstrap.releaseExternalResources();
		logger.info("shutting down PolicyServer...");
		ChannelFuture policyF = policy.close();
		policyF.awaitUninterruptibly();
		policyBootstrap.releaseExternalResources();
		logger.info("shutting down NodeServer...");
		rpcServer.shutdown();
	}

	private ChannelPipelineFactory getChannelPipelineFactory() {
		return new ChannelPipelineFactory() {
			Timer timer = new HashedWheelTimer();

			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decode", context.getBean(MessageDecoder.class));
				pipeline.addLast("encode", context.getBean(MessageEncoder.class));
				pipeline.addLast("timeout", new IdleStateHandler(timer, 180, 0, 0));// read,
																					// write,
																					// both
				pipeline.addLast("hearbeat", context.getBean(HeartbeatHandler.class));
				pipeline.addLast("handler", context.getBean(MessageHandler.class));
				return pipeline;
			}
		};
	}

	private ChannelPipelineFactory getPolicyPipelineFactory() {
		return new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024, Delimiters.nulDelimiter()));// amf格式,必须要用\0
				pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
				pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
				pipeline.addLast("handler", context.getBean(PolicyServerHandler.class));
				return pipeline;
			}
		};
	}
}
