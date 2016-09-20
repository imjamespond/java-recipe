package com.metasoft.flying.net.client;

import java.net.SocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.vo.LoginRequest;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

public class NettyClient {
	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
	private static final GeneralRequest heartbeat = new GeneralRequest();
	
	static {
		logger.debug("NettyClient");
		heartbeat.setCmd("enroll.info");
		heartbeat.setSerial(2);
	}
	private int id = 0;
	private ClientBootstrap bootstrap;
	private NettyChannelPipelineFactory channelPipelineFactory;
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

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public NettyClient(int i, ChannelFactory channelFactory) {
		channelPipelineFactory = new NettyChannelPipelineFactory(this);
        bootstrap = new ClientBootstrap(channelFactory);
        bootstrap.setPipelineFactory(channelPipelineFactory);
        id = i;
	}
	
    public void startConnect(SocketAddress addr) {
        ChannelFuture future = bootstrap.connect(addr);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture cf) throws Exception {
                synchronized(heartbeat) {
                	heartbeat.notify();
                }
            	
                if(cf.getCause()!=null){
                	logger.debug(cf.getCause().getMessage());
                }else{
                	logger.debug("connected!"+String.valueOf(id));
                	channel = cf.getChannel();

    				LoginRequest req = new LoginRequest();
    				req.setCmd("login.login");
    				req.setSerial(1);
    				req.setName("a"+String.valueOf(id));
    				req.setGender(GeneralConstant.FEMALE);
    				channel.write(req);
                }
            }
        });
        
        //current thread owns object's monitor, To do that, you must synchronize on it:
        try {
            synchronized(heartbeat) {
            	heartbeat.wait(10000l);
            }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void onMessageRecv(Object obj){
    	if(obj instanceof GeneralResponse){
			GeneralResponse resp = (GeneralResponse) obj;
			int serial = resp.getSerial();
			
			switch(serial){
			case 1:
				//UserVO vo = (UserVO) resp.getData();
				//logger.debug(vo.getName());
				
//				if(vo.getGender() == 0){
//					LoginRequest req1 = new LoginRequest();
//					req1.setCmd("login.change");
//					req1.setSerial(1);
//					req1.setName("如花"+String.valueOf(num));
//					req1.setGender(GeneralConstant.FEMALE);
//					
//					c.write(req1);							
//				}
//				
//				ChatRequest req = new ChatRequest();
//				req.setGroup(String.valueOf(uid));
//				req.setCmd("chat.join");
//				req.setSerial(3);
//				
//				c.write(req);
				break;
			}
		}    	
    }

    public void stop() {
        bootstrap.releaseExternalResources();
    }

    public void keepAlive(){
    	if(null!=channel && channel.isWritable()){	
			channel.write(heartbeat);
    	}
	}
    
    public void sendRequest(GeneralRequest req){
    	if(null!=channel && channel.isWritable()){
    		channel.write(req );
    	}
	}
}
