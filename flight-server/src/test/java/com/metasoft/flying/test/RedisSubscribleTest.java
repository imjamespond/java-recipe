package com.metasoft.flying.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.service.UserService;
import com.qianxun.service.ExchangeToWebService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("development")
public class RedisSubscribleTest {
    static{
    	UserService.debug = 1;
    }
	@Autowired
	ExchangeToWebService es;
	/*
	 * @Autowired GenericObjectPoolConfig jedisPoolConfig;
	 * 
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.port}") private int port;
	 * 
	 * @Value("${redis.timeout}") private int timeout;
	 * 
	 * @Value("${redis.password}") private String password;
	 */
	//byte[] key = "roseincanddec".getBytes();

	@Test
	public void serverTest() {
		es.subscribe();
		System.out.println("block here pleazz");
	}
}
