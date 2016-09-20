package com.qianxun.service;

import java.util.List;

import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.qianxun.model.BaseExchangeProtos;
import com.qianxun.model.BaseExchangeProtos.BaseExchange;
import com.qianxun.model.SubscribeThread;

@Component
public class ExchangeToWebService extends PublishService{
	public static final byte[] key = "exchange.to.web".getBytes();
	
	public static final String CMD_ROSE_EXCHANGE = "rose.exchange";
	public static final String CMD_GEM_EXCHANGE = "gem.exchange";
	
	@Autowired
	private RedisTemplate redisTemplate;
	//@Autowired
	//private RoseService roseService;
	
	private ExtensionRegistry registry = ExtensionRegistry.newInstance();
	
	//@PostConstruct
	public void init() {	
		BaseExchangeProtos.registerAllExtensions(registry);
	}
	
	public void put(BaseExchange exchange) {	
		redisTemplate.rpush(key, exchange.toByteArray());
	}

	
	@Override
	public void doJob(List<byte[]> list) {
		if (null != list) {
			int i = 0;
			for (byte[] bytes : list) {
				if(i++%2 == 0){
					continue;
				}
				
				BaseExchange protoMsg;
				try {
					protoMsg = BaseExchange.parseFrom(bytes,registry);
					if(protoMsg.getType().indexOf(ExchangeToWebService.CMD_ROSE_EXCHANGE)>=0){	
						System.out.printf("test\n");
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public byte[] getKey() {
		return ExchangeToWebService.key;
	}

	@Override
	public void subscribe() {		
		Thread thread = new SubscribeThread(this);
		thread.start();
	}

	@Override
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}
}