package com.qianxun.service;

import java.util.List;

import org.copycat.framework.nosql.RedisTemplate;

public abstract class PublishService {

	public abstract RedisTemplate getRedisTemplate();
	
	/**
	 * 订阅到的消息处理
	 * @param list
	 */
	public abstract void doJob(List<byte[]> list);
	
	/**
	 * redis的key
	 * @return
	 */
	public abstract byte[] getKey();
	
	/**
	 * 订阅
	 */
	public abstract void subscribe();
}