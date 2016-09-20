package com.test.qianxun.web;

import java.util.List;

import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.qianxun.model.Constant;
import com.qianxun.model.job.VoteJob;
import com.qianxun.service.VoteJobService;
import com.qianxun.util.JsonLite;
import com.test.qianxun.model.VoteLite;
import com.test.qianxun.service.GameService;
import com.test.qianxun.service.RankService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.VoteLiteService;
import com.test.qianxun.util.EpochUtil;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/vote")
public class VoteController {
	@Autowired
	private VoteLiteService vlService;
	@Autowired
	private GameService gameService;
	@Autowired
	private RankService rankService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private VoteJobService vjService;
	@Autowired
	RedisTemplate redisTemplate;
	//private int total = 50;
	
	@RequestMapping("")
  	public String index(Model model) {
		model.addAttribute("listData", glist());
		model.addAttribute("resultData", info());
		return "vote/index";
 	}

	@RequestMapping("/{id}/{type}/{rank}")
	@ResponseBody
	public String vote( @PathVariable long id,
			@PathVariable int type,
			@PathVariable int rank) {
		String suid = sessionService.getUid();
		if (suid == null) {
			return "need login";
		} else {
			long uid = Long.parseLong(suid);
			vjService.produce(new VoteJob(uid, id, type, rank));
			return "ok";
		}
	}
	
	@RequestMapping("/info")
	@ResponseBody
	public String info() {
		String suid = sessionService.getUid();
		if (suid == null) {
			return "need login";
		} else {
			long uid = Long.parseLong(suid);
			VoteLite voteLite = vlService.get(uid);
			JsonLite json = new JsonLite(JsonLite.Type.Bracket);
			try {
				//initialize json generator 
				StringBuilder sb = new StringBuilder(); 
				//get gid array
				long voteArr[] = null;
				int week = EpochUtil.getEpochWeek(System.currentTimeMillis());
				if(null==voteLite){
					voteArr = new long[VoteJobService.VOTE_SIZE];
				}else if(week!=voteLite.getEpochweek()){
					voteArr = new long[VoteJobService.VOTE_SIZE];
				}else{
					voteArr = JsonUtils.toObject(long[].class, voteLite.getVote());
				}
				//prepare keys
				String keys[] = new String[voteArr.length];
				for(int i=0; i< voteArr.length; i++){
					keys[i] = Constant.GAME_NAME+voteArr[i];
				}
				//query name of game
				Jedis jedis = redisTemplate.getResource();
				List<String> list = jedis.mget(keys);
				redisTemplate.returnResource(jedis);
				sb.append("{");
				for(int i=0; i< voteArr.length; i++){				
					String name = list.get(i);
					JsonLite jl = new JsonLite();
					jl.appendKeyValue(String.valueOf(voteArr[i]),name==null?"?":name);
					json.appendNodeString(jl.convert2String());
				}
			} catch (Exception e) {
			}
			return json.convert2String();
		}
	}
	
	@RequestMapping("/glist")
	@ResponseBody
	public String glist() {
		Jedis jedis = redisTemplate.getResource();
		String rs = jedis.get(Constant.GAME_LIST);
		redisTemplate.returnResource(jedis);
		return rs;
	}
	
//	@RequestMapping("")
//	public String index(Model model) {
//		return get(model, 0, 0);
//	}
//
//	@RequestMapping("/{type}/{rank}")
//	public String get(Model model, @PathVariable int type,
//			@PathVariable int rank) {
//		String suid = sessionService.getUid();
//		if (suid == null) {
//			return "redirect:/user/login";
//		} else {
//			long uid = Long.parseLong(suid);
//			Calendar calendar = Calendar.getInstance();
//			calendar.set(Calendar.HOUR_OF_DAY, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			calendar.set(Calendar.DAY_OF_WEEK, 1);
//			long start = calendar.getTimeInMillis();
//			long end = System.currentTimeMillis();
//			List<Rank> rankList = rankService.listByVotes(type, rank, total);
//			List<Vote> voteList = voteService.listByUid(uid, type, start, end);
//			boolean isVoted = voteService
//					.checkVote(uid, type, rank, start, end);
//			if (isVoted) {
//				Vote vote = voteService.findByUid(uid, type, rank, start, end);
//				model.addAttribute("vote_gid", vote.getGid());
//			}else{
//				Vote vote = voteService.findLastVote(uid, type, rank, start);
//				if(vote != null){
//					model.addAttribute("vote_gid", vote.getGid());
//				}
//			}
//			model.addAttribute("rankList", rankList);
//			model.addAttribute("voteList", voteList);
//			model.addAttribute("type", type);
//			model.addAttribute("rank", rank);
//			model.addAttribute("isVoted", isVoted);
//			return "vote/index";
//		}
//	}
//
//	@RequestMapping(value = "/save", method = RequestMethod.POST)
//	@ResponseBody
//	public String save(Vote vote) {
//		Game game = gameService.get(vote.getGid());
//		if (game != null) {
//			long uid = Long.parseLong(sessionService.getUid());
//			Calendar calendar = Calendar.getInstance();
//			calendar.set(Calendar.HOUR_OF_DAY, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			calendar.set(Calendar.DAY_OF_WEEK, 1);
//			long start = calendar.getTimeInMillis();
//			long end = System.currentTimeMillis();
//			boolean isVoted = voteService.checkVote(uid, vote.getType(),
//					vote.getRank(), start, end);
//			if (isVoted) {
//				return "1";
//			} else {
//				vote.setUid(uid);
//				vote.setGname(game.getGname());
//				vote.setVotetime(System.currentTimeMillis());
//				voteService.save(vote);
//				rankService.vote(vote.getGid(), vote.getType(), vote.getRank());
//				return "2";
//			}
//		} else {
//			return "3";
//		}
//	}
}