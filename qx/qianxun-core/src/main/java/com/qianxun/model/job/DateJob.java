package com.qianxun.model.job;

import org.copycat.framework.nosql.RedisTemplate;

import redis.clients.jedis.Jedis;

import com.qianxun.model.Constant;
import com.qianxun.model.FlyUser;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.SpringService;
import com.qianxun.util.JsonLite;

public class DateJob extends IJob{
	long uid;
	String date;
	public DateJob(long uid,String date) {
		this.uid = uid;
		this.date = date;
	}

	@Override
	public void doJob() {
		FlyUserService fupService = SpringService.getBean(FlyUserService.class);
		FlyUser fup = fupService.get(uid);
		if(fup!=null){
			String nickname = fup.getNickname();
			if(nickname!=null){			
				JsonLite json = new JsonLite();
				json.appendKeyValue("name", nickname);
				json.appendKeyValue("uid", String.valueOf(uid));
				json.appendKeyValue("date", date);
				RedisTemplate redisTemplate = SpringService.getBean( RedisTemplate.class );
				Jedis jedis = redisTemplate.getResource();
				Long num = jedis.lpush(Constant.GAME_DATE, json.convert2String());//住前加
				if(num > 50){
					jedis.rpop(Constant.GAME_DATE);
				}
				redisTemplate.returnResource(jedis);
			}
		}
		
	}

}
