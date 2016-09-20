package com.metasoft.flying.net;

import java.net.SocketAddress;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.vo.ChatRequest;
import com.metasoft.flying.vo.general.GeneralRequest;

public class NettyClient {
	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
	private static final GeneralRequest heartbeat = new GeneralRequest();
	static {
		logger.debug("NettyClient");
		heartbeat.setCmd("item.info");
		heartbeat.setSerial(2);
	}
	private ClientBootstrap bootstrap;
	private NettyChannelPipelineFactory channelPipelineFactory;
	private ScheduledExecutorService scheduledExecutorService;
	private Channel channel;
	public ClientBootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(ClientBootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public NettyChannelPipelineFactory getChannelPipelineFactory() {
		return channelPipelineFactory;
	}

	public void setChannelPipelineFactory(NettyChannelPipelineFactory channelPipelineFactory) {
		this.channelPipelineFactory = channelPipelineFactory;
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}

	public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public NettyClient(NettyChannelPipelineFactory pipelineFactory, ChannelFactory channelFactory, ScheduledExecutorService scheduledExecutorService) {
		this.channelPipelineFactory = pipelineFactory;
		this.scheduledExecutorService = scheduledExecutorService;
        bootstrap = new ClientBootstrap(channelFactory);
        bootstrap.setPipelineFactory(channelPipelineFactory);
	}
	
    public void startConnect(SocketAddress addr) {
        ChannelFuture future = bootstrap.connect(addr);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture cf) throws Exception {
              
                if(cf.getCause()!=null){
                	System.err.println(cf.getCause().getMessage());
                }else{
                	//System.err.println("connected!");
                	channel = cf.getChannel();
                	keepAlive();
                }
            }
        });
    }

    public void stop() {
        bootstrap.releaseExternalResources();
    }

    public void keepAlive(){
    	if(null!=channel && channel.isWritable()){
    		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
    			public void run() {
  			
    					ChatRequest req = new ChatRequest();
    					req.setGroup(String.valueOf(channelPipelineFactory.getUid()));
    					req.setCmd("chat.broadcast");
    					req.setSerial(5);
    					req.setMsg("大家好 我是 "+channelPipelineFactory.getName());
    					channel.write(req);
    			}
    		}, 1000L, 15000L, TimeUnit.MILLISECONDS);
    	}
	}
    
    public void sendRequest(GeneralRequest req){
    	if(null!=channel && channel.isWritable()){
    		channel.write(req );
    	}
	}
}
