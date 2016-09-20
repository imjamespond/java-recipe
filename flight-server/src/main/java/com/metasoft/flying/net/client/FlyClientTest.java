package com.metasoft.flying.net.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

public class FlyClientTest {
	private static InetSocketAddress addr = new InetSocketAddress("fly.qianxunyouxi.com", 7002);
	private List<NettyClient> clientList = new ArrayList<NettyClient>();
	private HashedWheelTimer hashTimer = new HashedWheelTimer();//Executors.newScheduledThreadPool(8);//Executors.newSingleThreadScheduledExecutor();
	
	public FlyClientTest(){
	}
	
	public void test(){
        ChannelFactory channelFactory = new NioClientSocketChannelFactory(
                Executors.newFixedThreadPool(1),
                Executors.newCachedThreadPool(), 4);
        
        
		for(int i = 0;i<5000;++i){
			final NettyClient client = new NettyClient(i, channelFactory);
			client.startConnect(addr);
			clientList.add(client);
			
			hashTimer.newTimeout(new TimerTask(){
				@Override
				public void run(Timeout timeout) throws Exception {
					client.keepAlive();
					hashTimer.newTimeout(this, 30, TimeUnit.SECONDS);
				}
			}, 45, TimeUnit.SECONDS);
		}
		
		
	}
	
	public static void main(String[] args) {
		FlyClientTest client = new FlyClientTest();
		client.test();
	}
}