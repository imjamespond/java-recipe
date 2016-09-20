package com.metasoft.flying.test;

import org.copycat.framework.nosql.RedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.model.FlightRp;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("home-dev")
public class RedisLuaTest {
    static{
    	UserService.debug = 1;
    }
	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void serverTest() throws GeneralException {
		long before = System.currentTimeMillis();
		/*
		Jedis jedis = redisTemplate.getResource();
		
	    List<String> keys = new ArrayList<String>();
	    keys.add("rank_rp_today");
	    keys.add("rank_rp_lastday");
	    keys.add("foo");

	    List<String> args = new ArrayList<String>();
	    args.add("2");
	    args.add("1");

	    String lua = "local val=tonumber(ARGV[2]) "
	    		+ "local score=redis.call('ZSCORE', KEYS[1], KEYS[3]) "
	    		+ "if score then "
	    		+ "local oldval=tonumber(score) "
	    		+ "if val<oldval then "
	    		+ "redis.call('ZREM', KEYS[1], KEYS[3]) "
	    		+ "redis.call('ZADD', KEYS[1], val, KEYS[3]) "
	    		+ "end "
	    		+ "else "
	    		+ "redis.call('ZADD', KEYS[1], tonumber(ARGV[2]), KEYS[3]) "
	    		+ "end "
	    		+ "if redis.call('ZCARD', KEYS[1])>tonumber(ARGV[1]) then "
	    		+ "return redis.call('ZREMRANGEBYRANK',KEYS[1],-1,-1) "
	    		+ "end "
	    		+ "return 'ok' ";
	    System.out.println( jedis.eval(lua,keys,args));
	    
	    //System.out.println( jedis.zrangeByScore("rank_rp_today", 0, Double.MAX_VALUE));
	    System.out.println( jedis.zrange("rank_rp_today", 0, -1));*/
		
		for(int i=0; i<100; i++){
			//FlightRp.addRpRank(10000l+i, i*10);
		}
		FlightRp.getRpRank(FlightRp.kRpRankToday);

		System.out.println(System.currentTimeMillis()-before);
	}
}
