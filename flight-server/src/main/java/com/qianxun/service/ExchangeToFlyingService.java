package com.qianxun.service;

import java.util.List;

import org.copycat.framework.nosql.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserWealthPersist;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.UserWealthService;
import com.metasoft.flying.vo.general.GeneralResponse;
import com.qianxun.model.BaseExchangeProtos;
import com.qianxun.model.BaseExchangeProtos.BaseExchange;
import com.qianxun.model.BaseExchangeProtos.GemExchange;
import com.qianxun.model.BaseExchangeProtos.RoseExchange;
import com.qianxun.model.SubscribeThread;

@Component
public class ExchangeToFlyingService extends PublishService {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeToFlyingService.class);
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private UserWealthService userWealthService;
	private static final String key = "exchange.to.flying";

	private ExtensionRegistry registry = ExtensionRegistry.newInstance();
	
	//@PostConstruct
	public void init() {	
		BaseExchangeProtos.registerAllExtensions(registry);
	}
	public void put(BaseExchange exchange) {
		// redisTemplate.rpush(getKey(), exchange.toByteArray());
	}

	@Override
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	@Override
	public void doJob(List<byte[]> list) {
		if (null != list) {
			int i = 0;
			for (byte[] bytes : list) {
				if (i++ % 2 == 0) {
					continue;
				}

				BaseExchange protoMsg;
				try {
					protoMsg = BaseExchange.parseFrom(bytes, registry);
					if (protoMsg.getType().indexOf(ExchangeToWebService.CMD_ROSE_EXCHANGE) >= 0) {
						RoseExchange exchange = protoMsg.getExtension(RoseExchange.roseExchange);
						try {
							User user = userService.getAnyUserById(exchange.getUid());
							UserWealthPersist userWealth = userWealthService.get(exchange.getUid());
							if(null!=userWealth){
								user.setUserWealthPersist(userWealth);
							}
							if (user.getConn() != null) {
								user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
							}
							logger.info(String.format("RoseExchange uid:%d num:%d, description:%s", exchange.getUid(), exchange.getCount(), exchange.getDiscription()));
						} catch (GeneralException e) {
							e.printStackTrace();
						}

					}else if (protoMsg.getType().indexOf(ExchangeToWebService.CMD_GEM_EXCHANGE) >= 0) {
						GemExchange exchange = protoMsg.getExtension(GemExchange.gemExchange);
						try {
							User user = userService.getAnyUserById(exchange.getUid());
							UserWealthPersist userWealth = userWealthService.get(exchange.getUid());
							if(null!=userWealth){
								user.setUserWealthPersist(userWealth);
							}
							if (user.getConn() != null) {
								user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
							}
							logger.info(String.format("GemExchange uid:%d, num:%d, description:%s", exchange.getUid(), exchange.getCount(), exchange.getDiscription()));
						} catch (GeneralException e) {
							e.printStackTrace();
						}						
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
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