package com.qianxun.service;

import java.util.List;

import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.qianxun.model.Constant;
import com.qianxun.util.JsonLite;
import com.test.qianxun.model.Game;
import com.test.qianxun.service.GameService;

import redis.clients.jedis.Jedis;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	RedisTemplate redisTemplate;
	@Autowired
	private GameService gameService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent e) {
		XmlWebApplicationContext xml = (XmlWebApplicationContext) e.getSource();
		if( null==xml.getNamespace() || xml.getNamespace().indexOf("app-servlet") < 0 ){
			return;
		}
		
		//System.out.println("startup");
		JsonLite jsonGameArray = new JsonLite(JsonLite.Type.Bracket);
		Jedis jedis = redisTemplate.getResource();
		List<Game> gameList = gameService.list();
		for(Game game:gameList){
			//id->name
			jedis.set(Constant.GAME_NAME+game.getId(), game.getGname());
			//game list
			JsonLite jsonGame = new JsonLite();
			jsonGame.appendKeyValue("gid", String.valueOf(game.getId()));
			jsonGame.appendKeyValue("name", game.getGname());
			jsonGame.appendKeyValue("type", String.valueOf(game.getType()));
			jsonGameArray.appendNodeString(jsonGame.convert2String());
		}
		jedis.set(Constant.GAME_LIST, jsonGameArray.convert2String());
		redisTemplate.returnResource(jedis);
		//String json = jedis.get(Constant.GAME_INFO);		
		//System.out.println(json);
		//Map<String, Object> map = JsonUtils.toMap(json);
		//FIXME debug
//		for(Entry<String, Object> entry : map.entrySet()){
//			Object obj = entry.getValue();
//			if(obj instanceof Game){
//				Game g=(Game) obj;
//				System.out.println(JsonUtils.toJson(g));
//			}
//		}
		//FIXME debug
//		String keys[] = {Constant.GAME_NAME+"335",Constant.GAME_NAME+"332"};
//		List<String> list = jedis.mget(keys);
//		System.out.println(list.toString());
	}

}
