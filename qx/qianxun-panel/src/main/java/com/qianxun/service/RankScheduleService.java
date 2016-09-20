package com.qianxun.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.copycat.framework.nosql.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.qianxun.model.Constant;
import com.qianxun.service.ScheduleService;
import com.qianxun.service.SpringService;
import com.test.qianxun.model.GameEx;
import com.test.qianxun.model.RankLite;
import com.test.qianxun.service.GameExService;
import com.test.qianxun.service.GameVoteService;
import com.test.qianxun.service.RankService;
import com.test.qianxun.util.JsonUtils;

@Service
public class RankScheduleService {
	private static final Logger logger = LoggerFactory.getLogger(RankScheduleService.class);

	private static final int SIZE = 10;

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private GameExService gameService;
	@Autowired
	private GameVoteService gvService;
	@Autowired
	private RankService rankService;
	@Value("${rank.json}")
	private String rankJson;
	
	@PostConstruct
	public void init() {
		Runnable runnable1 = new RankTask(1);
		scheduleService.schedule(runnable1, "0 30 3 * * ?");
		Runnable runnable2 = new RankTask(2);
		scheduleService.schedule(runnable2, "0 0 * * * ?");
		//scheduleService.scheduleAtFixedRate(runnable, 5000l, 300000l);
	}
	
	public void rank() {
		Map<String,List<RankLite>> map = new HashMap<String,List<RankLite>>();
		for(int j = 0;j < 3; j++){//类型
			for (int i = 0; i < 8; i++) {//榜
				List<GameEx> list = gameService.listByVotes(j, i, SIZE);
				List<RankLite> newList = new ArrayList<RankLite>();
				int count = 0;
				for(GameEx game:list){
					RankLite rl = new RankLite(game.getId(), game.getGname(), j, i, game.getVote(), game.getRank());
					gvService.updateTrend(game.getId(),i,count);
					newList.add(rl);
					count++;
				}
				map.put("rankList" + j + i,newList);
			}
		}
		String json = JsonUtils.toJson(map);
		RedisTemplate redisTemplate = SpringService.getBean( RedisTemplate.class );
		Jedis jedis = redisTemplate.getResource();
		jedis.set(Constant.GAME_RANK,json);
		redisTemplate.returnResource(jedis);
		write2JsonFile(json);
	}
	
	public void rankWithoutTrend() {
		Map<String,List<RankLite>> map = new HashMap<String,List<RankLite>>();
		for(int j = 0;j < 3; j++){//类型
			for (int i = 0; i < 8; i++) {//榜
				List<GameEx> list = gameService.listByVotes(j, i, SIZE);
				List<RankLite> newList = new ArrayList<RankLite>();
				//int count = 0;
				for(GameEx game:list){
					RankLite rl = new RankLite(game.getId(), game.getGname(), j, i, game.getVote(), game.getRank());
					//gvService.updateTrend(game.getId(),i,count);
					newList.add(rl);
					//count++;
				}
				map.put("rankList" + j + i,newList);
			}
		}
		String json = JsonUtils.toJson(map);
		RedisTemplate redisTemplate = SpringService.getBean( RedisTemplate.class );
		Jedis jedis = redisTemplate.getResource();
		jedis.set(Constant.GAME_RANK, json);
		redisTemplate.returnResource(jedis);
		write2JsonFile(json);
	}
	
	private void write2JsonFile(String json){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter (new FileWriter(rankJson));	
			writer.write(json);
			writer.flush();
			writer.close();	
			
		} catch (IOException e) {
			System.out.println("ERROR: empty Feature data");
			return;
		}
	}

	private class RankTask implements Runnable {

		private int type;

		public RankTask(int i) {
			type = i;
		}

		@Override
		public void run() {
			logger.debug("RankScheduleService");
			if(type==1)
				rank();
			else if(type==2)
				rankWithoutTrend();
		}

	}

}
