package com.qianxun.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.qianxun.model.BaseExchangeProtos;
import com.qianxun.model.BaseExchangeProtos.BaseExchange;
import com.qianxun.model.BaseExchangeProtos.GemExchange;
import com.qianxun.model.BaseExchangeProtos.RoseExchange;
import com.qianxun.model.Constant;
import com.qianxun.model.LoggerPersist;
import com.qianxun.model.SubscribeThread;

@Component
public class ExchangeToWebService extends PublishService{
	public static final byte[] key = "exchange.to.web".getBytes();
	
	public static final String CMD_ROSE_EXCHANGE = "rose.exchange";
	public static final String CMD_GEM_EXCHANGE = "gem.exchange";
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private LoggerPersistService loggerService;
	
	private ExtensionRegistry registry = ExtensionRegistry.newInstance();
	
	@PostConstruct
	public void init() {	
		BaseExchangeProtos.registerAllExtensions(registry);
	}
	
	public void put(BaseExchange exchange) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("count", exchange.getCount());
//		map.put("uid", exchange.getUid());
//		map.put("trend", exchange.getTrend());
//		map.put("discription", exchange.getDiscription());
		
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
					LoggerPersist logger = new LoggerPersist();
					logger.setLogDate(System.currentTimeMillis());
					
					if(protoMsg.getType().indexOf(ExchangeToWebService.CMD_ROSE_EXCHANGE)>=0){
						RoseExchange roseExchange = protoMsg.getExtension(RoseExchange.roseExchange);						
						logger.setUid(roseExchange.getUid());						
						logger.setDiscription(roseExchange.getDiscription());
						logger.setNum(roseExchange.getCount());
						logger.setType(Constant.TYPE_ROSE);										
					}else if(protoMsg.getType().indexOf(ExchangeToWebService.CMD_GEM_EXCHANGE)>=0){
						GemExchange exchange = protoMsg.getExtension(GemExchange.gemExchange);						
						logger.setUid(exchange.getUid());						
						logger.setDiscription(exchange.getDiscription());
						logger.setNum(exchange.getCount());
						logger.setType(Constant.TYPE_GEM);							
					}
					
					loggerService.save(logger);	
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