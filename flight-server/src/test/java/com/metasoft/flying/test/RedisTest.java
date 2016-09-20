package com.metasoft.flying.test;

import org.copycat.framework.nosql.RedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.metasoft.flying.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("home-dev")
public class RedisTest {
    static{
    	UserService.debug = 1;
    }
	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void serverTest() {
		long before = System.currentTimeMillis();
		
		Jedis jedis = redisTemplate.getResource();
		int limit = 999999;
		while(limit-->0){
			jedis.lpush("limit-1234567890", "foobar");
		}
		redisTemplate.returnResource(jedis);
		System.out.println(System.currentTimeMillis()-before);
	}
}
