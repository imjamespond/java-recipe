package com.metasoft.flying.model.job;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.copycat.framework.nosql.RedisTemplate;

import redis.clients.jedis.Jedis;

import com.metasoft.flying.service.common.IJob;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.util.JsonUtils;
import com.metasoft.flying.vo.MatchPlayerVO;

public class MatchRankJob extends IJob{

	private List<MatchPlayerVO> matchDayRank;
	public MatchRankJob(List<MatchPlayerVO> list) {
		matchDayRank = list;
	}

	@Override
	public void doJob() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String day = String.valueOf(c.get(Calendar.DAY_OF_YEAR));
		RedisTemplate redisTemplate = SpringService.getBean( RedisTemplate.class );
		
		if(matchDayRank.size()>0){
			Jedis jedis = redisTemplate.getResource();
			String key = "match-rank:"+year+"-"+day;
			jedis.set(key, JsonUtils.toJson(matchDayRank));
			jedis.expire(key, 240*3600);
			redisTemplate.returnResource(jedis);
		}
//		if(matchDayRankMap.size()>0){
//			Map<String, String> map2 = new HashMap<String, String>();
//			for(Entry<Long, Integer> entry : matchDayRankMap.entrySet()){
//				map2.put("uid:"+entry.getKey(), String.valueOf(entry.getValue()));
//			}
//			//redisTemplate.hmset("match-my-rank:"+year+"-"+day, map2);
//			Jedis jedis = redisTemplate.getResource();
//			String key = "match-my-rank:"+year+"-"+day;
//			jedis.set(key, JsonUtils.toJson(map2));
//			jedis.expire(key, 240*3600);
//		}
	}

	public byte[] toByte(Map<String, String> map) {
		String json = JsonUtils.toJson(map);
		try {
			return json.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
