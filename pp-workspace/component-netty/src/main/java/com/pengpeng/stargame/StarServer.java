package com.pengpeng.stargame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import com.pengpeng.stargame.impl.RunnableServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.pengpeng.ServerConfig;

//@ManagedResource()
//@Component("mingXingServer")
public class StarServer {
	private static Log logger = LogFactory.getLog(StarServer.class);

	public StarServer(){
		
	}
	public static void main(String[] args) throws UnknownHostException{
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"context-stargame.xml","beanRefPlayerDataAccess.xml"});
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
		context.scan("com.pengpeng.stargame","org.springframework");
//		context.refresh();
        RunnableServer foxcomServer = context.getBean(RunnableServer.class);
        foxcomServer.setListener(context.getBean(IConnectListener.class));
//--------------------------------------------------------------------------------
//--------------------------------------------------------------------------------
		foxcomServer.start();
        if (args.length>0 && "security".equalsIgnoreCase(args[0])){
		    startSecurityServer(context);
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
	public static void startGameServer(SimpleChannelHandler server){
    	NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(Executors
                .newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("reuseAddress", true);

        bootstrap.getPipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1024, 0,4));
//        bootstrap.getPipeline().addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024, Delimiters.nulDelimiter()));//amf格式,必须要用\0
//      bootstrap.getPipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
//      bootstrap.getPipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
        AMF3Decoder decoder = new AMF3Decoder();
        bootstrap.getPipeline().addLast("decoder", decoder);
        bootstrap.getPipeline().addLast("encoder", new AMF3Encoder());
        bootstrap.getPipeline().addLast("handler", server);

        bootstrap.bind(new InetSocketAddress(6666));
        logger.info("START|LISION PORT:" + 6666);
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
