package com.qianxun.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qianxun.model.BaseExchangeProtos.BaseExchange;
import com.qianxun.model.SubscribeThread;
import com.test.qianxun.service.UserService;

@Component
public class ExchangeToFlyingService extends PublishService{
	@Autowired 
	private RedisTemplate redisTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private ExchangeToWebService webService;
	private static final String key = "exchange.to.flying";
	
	@PostConstruct
	public void init() {
		webService.subscribe();
	}
	
	public void put(BaseExchange exchange) {
		redisTemplate.rpush(getKey(), exchange.toByteArray());
	}

	@Override
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	@Override
	public void doJob(List<byte[]> list) {
		
	}

	@Override
	public byte[] getKey() {
		return ExchangeToFlyingService.key.getBytes();
	}

	@Override
	public void subscribe() {		
		Thread thread = new SubscribeThread(this);
		thread.start();
	}
}