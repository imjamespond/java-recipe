package com.pengpeng.stargame;

import com.pengpeng.stargame.impl.RunnableServer;
import com.pengpeng.stargame.managed.NodeConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

public class LoginServer {
	private static Log logger = LogFactory.getLog(LoginServer.class);

	public LoginServer(){
		
	}
	public static void main(String[] args) throws UnknownHostException{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"context-frontend.xml"});
		//AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
		//context.scan("com.pengpeng.stargame","org.springframework");
//		context.refresh();
        RunnableServer foxcomServer = context.getBean(RunnableServer.class);
//--------------------------------------------------------------------------------
//--------------------------------------------------------------------------------
        try{
//        if (args.length>0 && "security".equalsIgnoreCase(args[0])){
            startSecurityServer(context);
//        }
        }catch (Exception e){
            e.printStackTrace();
        }
		startGameServer((SimpleChannelHandler)foxcomServer);
        // 注删jvm关闭钩子,发出关闭事件
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	logger.info("START|ShutdownHook Registry.");
            }
        });
	}
	public static void startGameServer(final SimpleChannelHandler server){
    	NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(Executors
                .newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("reuseAddress", true);

        //bootstrap.getPipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,4));
//        bootstrap.getPipeline().addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024, Delimiters.nulDelimiter()));//amf格式,必须要用\0
//      bootstrap.getPipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
//      bootstrap.getPipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory(){
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,4));
                pipeline.addLast("decoder", new AMF3Decoder());
                pipeline.addLast("encoder", new AMF3Encoder());
                pipeline.addLast("handler", server);
                return pipeline;
            }
        });
        bootstrap.bind(new InetSocketAddress(NodeConfig.getTcpPort()));
        logger.info("START|LISION PORT:" + NodeConfig.getTcpPort());
	}
	
	public static void startSecurityServer(ApplicationContext ctx){
    	NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(Executors
                .newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("reuseAddress", true);

//        bootstrap.getPipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1024, 0,4));
        bootstrap.getPipeline().addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024, Delimiters.nulDelimiter()));//amf格式,必须要用\0
        bootstrap.getPipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
        bootstrap.getPipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
        bootstrap.getPipeline().addLast("handler", ctx.getBean(SecurityServerHandler.class));

        bootstrap.bind(new InetSocketAddress(843));
        logger.info("START|LISION PORT:" + 843);
	}
////////////////////////////////////////////////////////////////////////////////	
}
