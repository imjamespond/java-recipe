package com.metasoft.flying.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.ScheduleService;
import com.qianxun.service.ExchangeToWebService;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"/applicationContext.xml"})  
@ActiveProfiles("development")
public class RedisPublishTest {
	@Autowired
	ExchangeToWebService es;
	@Autowired
	private UserService userService;
/*	GenericObjectPoolConfig jedisPoolConfig;
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.timeout}")
	private int timeout;
	@Value("${redis.password}")
	private String password;
	byte[] key = "roseincanddec".getBytes();
	JedisPool jedisPool; */	
	@Autowired
	ScheduleService schedule;

	
	@Test
	public void serverTest() throws GeneralException, InterruptedException{
		schedule.scheduleAtFixedRate(new Publish(), 1000l, 2000l);
		
		System.out.println("block here pleazz");
	}
	
    class Publish implements Runnable {

		@Override
		public void run() {
			try {
				User user = userService.getAnyUserById(101l);
				user.reduceRose(1, "test");
			} catch (GeneralException e) {
				e.printStackTrace();
			}
			System.out.println("publishing..");
		}

    }
    
    static{
    	UserService.debug = 1;
    }

}
