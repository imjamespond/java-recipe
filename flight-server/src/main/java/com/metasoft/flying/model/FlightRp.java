package com.metasoft.flying.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.copycat.framework.nosql.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.RpFinishVO;
import com.metasoft.flying.vo.RpRankVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class FlightRp extends Flight{
	private static final Logger logger = LoggerFactory.getLogger(FlightRp.class);
	private long commence;

	public FlightRp(GameRoom room) {
		super(room);
		type = GeneralConstant.GTYPE_RP;
	}
	
	@Override
	public void begin() {
		if (getPlayerNum() == 0) {
			logger.error("begin getPlayerNum is 0");
			return;
		}
		//设置rp 棋局
		for(ChessPlayer cp:chessPlayers){
			if(null==cp)
				continue;
			for(Chess chess:cp.getChesses()){
				chess.reset();
				//chess.setState(ChessConstant.CHESS_FINISH);
			}
			for(int i=0;i<cp.getItems().length;i++){
				cp.getItems()[i]=2;
			}
			//cp.getChesses()[0].setState(ChessConstant.CHESS_READY);
			//cp.getChesses()[1].setState(ChessConstant.CHESS_READY);
		}

		// 随机事件位置
		FlightHelper.randomCoord(this);

		commence();
		beginNotify();
		nextTurn(0);
	}
	
	/**
	 * 结算
	 */
	@Override
	public void finishNotify(int pos) {
		// 结束vo
		RpFinishVO rpFinishVO = getRpFinishVO(pos);
		broadcast(GeneralResponse.newObject(rpFinishVO));
		
		//广播
		if(0==pos){
			LocalizationService ls = SpringService.getBean(LocalizationService.class);
			UserService us = SpringService.getBean(UserService.class);
			ChessPlayer cp = chessPlayers[0];
			if(cp!=null&&cp.getUserId()>0){
				User user = us.getOnlineUserById(cp.getUserId());//玩家
				if(user!=null){
					List<RpRankVO> list = rpFinishVO.getList();
					int rank = 0;
					for(RpRankVO rp:list){
						rank++;
						if(rp.getUid() == user.getId() && rpFinishVO.getDuration() <= rp.getDuration()){//比自己成绩好					
							String[] strs = {user.getUserPersist().getNickname(),String.valueOf(rank)};
							ChatVO chat = new ChatVO(ls.getLocalString("rp.player", strs), -3, "");
							GeneralResponse resp = GeneralResponse.newObject(chat);
							String[] strs1 = {String.valueOf(rank)};
							ChatVO chat1 = new ChatVO(ls.getLocalString("rp.owner", strs1), -3, "");
							GeneralResponse resp1 = GeneralResponse.newObject(chat1);
							for(Entry<Long, User> enrty:us.getOnlineUserEntrySet()){//在线玩家广播
								User u = enrty.getValue();
								BaseConnection c = u.getConn();
								if(u.getId()==user.getId()){//
									c.deliver(resp1);
								}else{
									c.deliver(resp);
								}
							}
						}
					}
				}
			}
		}

		GameRoom gr = room.get();
		if(null!=gr){
			gr.setFlightRp(null);
		}
	}
	
	private RpFinishVO getRpFinishVO(int pos){
		RpFinishVO rpVO = new RpFinishVO();
		rpVO.setPos(pos);
		rpVO.setDuration(System.currentTimeMillis()-this.commence);
		if(chessPlayers[pos]!=null&&chessPlayers[pos].getUserId()>0&&pos==0){
			addRpRank(chessPlayers[pos].getUserId(),rpVO.getDuration());
		}
		rpVO.setList(getRpRank(kRpRankToday));
		return rpVO;
	}


	public long getCommence() {
		return commence;
	}

	public void commence() {
		this.commence = System.currentTimeMillis();
	}

	public static String kRpRankEpochday = "rp_rank_epochday";
	public static String kRpRankToday = "rp_rank_today";
	public static String kRpRankLastday = "rp_rank_last_today";
	private static String kRpRankSize = "30";
	public static void addRpRank(long uid,long duration){
		RedisTemplate redis = SpringService.getBean(RedisTemplate.class);
		Jedis jedis = redis.getResource();
		
	    List<String> keys = new ArrayList<String>();
	    keys.add(kRpRankToday);
	    keys.add(kRpRankLastday);    
	    keys.add(String.valueOf(uid));
	    //keys.add(kRpRankEpochday);

	    List<String> args = new ArrayList<String>();
	    args.add(kRpRankSize);
	    args.add(String.valueOf(duration));	    
	    //TODO check epoch day
	    //args.add(String.valueOf(EpochUtil.getEpochDay()));

	    String lua = 
	    		//判断epoch
	    		"local result='ok' "
//	    		+ "local epoch=tonumber(ARGV[3]) "
//	    		+ "local oldepoch=tonumber(redis.call('GET',KEYS[4])) "
//	    		+ "if oldepoch and oldepoch<epoch then "
//	    		+ "redis.call('ZREMRANGEBYRANK',KEYS[2],0,30) "
//	    		+ "redis.call('ZUNIONSTORE',KEYS[2],1,KEYS[1]) "
//	    		+ "redis.call('ZREMRANGEBYRANK',KEYS[1],0,30) "
//	    		+ "end "
	    		//对比分数更新
	    		+"local val=tonumber(ARGV[2]) "
	    		+ "local score=redis.call('ZSCORE', KEYS[1], KEYS[3]) "
	    		+ "if score then "
	    		+ "local oldval=tonumber(score) "
	    		+ "if val<oldval then "
	    		+ "redis.call('ZREM', KEYS[1], KEYS[3]) "
	    		+ "redis.call('ZADD', KEYS[1], val, KEYS[3]) "
	    		+ "result='update' "
	    		+ "end "
	    		+ "else "
	    		+ "redis.call('ZADD', KEYS[1], val, KEYS[3]) "
	    		+ "result='add' "
	    		+ "end "
	    		//限定size of the set
	    		+ "if redis.call('ZCARD', KEYS[1])>tonumber(ARGV[1]) then "
	    		+ "result=redis.call('ZREMRANGEBYRANK',KEYS[1],-1,-1) "
	    		+ "end "
	    		//+ "redis.call('SET',KEYS[4],epoch) "
	    		+ "return result ";
	    logger.debug("addRpRank uid:{}, jedis:{}",uid,jedis.eval(lua,keys,args));//TODO never comment this line
	    
	    redis.returnResource(jedis);
	}
	
	public static List<RpRankVO> getRpRank(final String key) {
		UserService us = SpringService.getBean(UserService.class);
		RedisTemplate redis = SpringService.getBean(RedisTemplate.class);
		Jedis jedis = redis.getResource();
		
		List<RpRankVO> list = new ArrayList<RpRankVO>(32);
		Set<Tuple> set = jedis.zrangeWithScores(key, 0, 30);
		for(Tuple tup : set){
			Long uid = Long.valueOf(tup.getElement());
			User user=null;
			try {
				user = us.getAnyUserById(uid);
			} catch (GeneralException e) {
			}
			if(null!=user){
				RpRankVO vo = new RpRankVO();
				vo.setDuration((long) tup.getScore());
				vo.setName(user.getUserPersist().getNickname());
				vo.setUid(user.getId());
				list.add(vo);
			}
			logger.debug("{} {}",uid,tup.getScore());
		}
		
		redis.returnResource(jedis);
		return list;
	}
	
	public static void awardRpRank() throws GeneralException{
		LocalizationService ls = SpringService.getBean(LocalizationService.class);
		UserService us = SpringService.getBean(UserService.class);
		RedisTemplate redis = SpringService.getBean(RedisTemplate.class);
		Jedis jedis = redis.getResource();
		
	    List<String> keys = new ArrayList<String>();
	    keys.add(kRpRankToday);
	    keys.add(kRpRankLastday);

	    List<String> args = new ArrayList<String>();
	    String lua = 
	    		//判断epoch
	    		"local result='ok' "
	    		+ "redis.call('ZREMRANGEBYRANK',KEYS[2],0,30) "
	    		+ "redis.call('ZUNIONSTORE',KEYS[2],1,KEYS[1]) "
	    		+ "redis.call('ZREMRANGEBYRANK',KEYS[1],0,30) ";
	    jedis.eval(lua,keys,args);
		
		int rank = 1;
		Set<Tuple> set = jedis.zrangeWithScores(FlightRp.kRpRankLastday, 0, 32);
		for(Tuple tup : set){
			Long uid = Long.valueOf(tup.getElement());
			User user = us.getAnyUserById(uid);
			if(null!=user){
				user.addCredit(5, ls.getLocalString("rp.award", new String[]{String.valueOf(rank++)}));
			}
			logger.debug("{} {}",uid,tup.getScore());
		}
		
		redis.returnResource(jedis);		
	}
}
