package com.qianxun.model;

import java.util.List;

import com.qianxun.service.PublishService;

import redis.clients.jedis.Jedis;

public class SubscribeThread extends Thread {

	private PublishService publish;

	public SubscribeThread(PublishService publish) {
		this.publish = publish;
	}

	@Override
	public void run() {
		while (true) {
			Jedis jedis = publish.getRedisTemplate().getResource();
			try{
				List<byte[]> list = jedis.blpop(0, publish.getKey());
				publish.doJob(list);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				publish.getRedisTemplate().returnResource(jedis);
			}
		}
	}

}