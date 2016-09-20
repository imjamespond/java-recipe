package com.metasoft.flying.test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.metasoft.flying.net.NettyChannelPipelineFactory;
import com.metasoft.flying.net.NettyClient;

public class FlyClientTest {
	private static InetSocketAddress addr = new InetSocketAddress("www1.qianxunyouxi.com", 7002);
	private List<NettyClient> clientList = new ArrayList<NettyClient>();
	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);//Executors.newSingleThreadScheduledExecutor();
	
	public FlyClientTest(){
        ChannelFactory channelFactory = new NioClientSocketChannelFactory(
                Executors.newFixedThreadPool(2),
                Executors.newFixedThreadPool(8));
		for(int i = 0;i<3000;++i){
			NettyClient client = new NettyClient(new NettyChannelPipelineFactory(i),channelFactory, scheduledExecutorService);
			client.startConnect(addr);
			clientList.add(client);
			try {
				Thread.sleep(100l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		FlyClientTest client = new FlyClientTest();
	}
}