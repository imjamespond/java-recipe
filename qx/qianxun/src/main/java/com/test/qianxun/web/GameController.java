package com.test.qianxun.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.qianxun.model.Game;
import com.test.qianxun.model.Rank;
import com.test.qianxun.service.GameService;
import com.test.qianxun.service.GameVoteService;
import com.test.qianxun.service.RankService;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/game")
public class GameController {
	@Autowired
	private GameService gameService;
	@Autowired
	private GameVoteService gvService;
	@Autowired
	private RankService rankService;
	
	/*js搜索
	@RequestMapping(value = "/multiplesearch2", method = RequestMethod.GET)
	@ResponseBody
	public String multipleSearch2(HttpServletRequest request) {	
		Map<Long, RankVote> rankMap = new HashMap<Long, RankVote>();
		// 1-3投票类型,4(端游,手游...)
		int type = 0;
		if (null != request.getParameter("type4")){
			type = Integer.valueOf(request.getParameter("type4"));
		}
		// rank1
		if (null != request.getParameter("type1")) {
			int rank = Integer.valueOf(request.getParameter("type1"));
			rank2(rank,type,rankMap);
		}
		// rank2
		if (null != request.getParameter("type2")) {
			int rank = Integer.valueOf(request.getParameter("type2"));
			rank2(rank,type,rankMap);
		}
		// rank3
		if (null != request.getParameter("type3")) {
			int rank = Integer.valueOf(request.getParameter("type3"));
			rank2(rank,type,rankMap);
		}
		// final rank
		Set<RankVote> rankFinal = new TreeSet<RankVote>();
		for (Entry<Long, RankVote> entry : rankMap.entrySet()) {
			RankVote rv = entry.getValue();
			rv.weight = rv.rank[0];
			rv.weight += rv.rank[1] * .8;
			rv.weight += rv.rank[2] * .5;
			rankFinal.add(rv);
		}
		// return rank
		List<RankVote> rankList = new ArrayList<RankVote>(5);
		int i = 0;
		for (RankVote tmpRank : rankFinal) {
			if (i++ < 5) {
				rankList.add(tmpRank);
			} else {
				break;
			}
		}
		return JsonUtils.toJson(rankList);
	}
	private static final int RANK_SIZE=4;
	private void rank2(int rank,int type, Map<Long, RankVote> rankMap){
		if (rank >= 0 && rank<RANK_SIZE) {
			int iRank = 0;
			//List<GameVote> list = gvService.listByRank(rank,type);
			for (GameVote gv : list) {
				RankVote rv = rankMap.get(gv.getGid());
				if (null != rv) {
					rv.rank[rank] = ++iRank;
				} else {
					rv = new RankVote();
					rv.gid = gv.getGid();
					rv.rank[rank] = ++iRank;
					rankMap.put(rv.gid, rv);
				}
			}
		}
	}
	
	class RankVote implements Comparable<RankVote> {
		public long gid;
		public int rank[] = new int[RANK_SIZE];
		public int weight;

		@Override
		public int compareTo(RankVote o) {
			return o.weight - weight;
		}
	}*/
	/**
	 * 多重搜索
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/multiplesearch", method = RequestMethod.GET)
	@ResponseBody
	public String multipleSearch(HttpServletRequest request) {
		int type4 = 0, iRank = 0;// 1-3投票类型,4(端游,手游...)

		if (null != request.getParameter("type4"))
			type4 = Integer.valueOf(request.getParameter("type4"));
		// List<Game> glist = gameService.list();
		List<Rank> rlist = rankService.list();
		Map<Long, TempRank> gameRankMap = new HashMap<Long, TempRank>();
		/*
		 * for (Game game : glist) { if (game.getType() != type4) { continue; }
		 * TempRank gameRank = new TempRank(); gameRank.game = game;
		 * gameRankMap.put(game.getId(), gameRank); }
		 */
		for (Rank rank : rlist) {
			if (rank.getType() != type4) {
				continue;
			}
			TempRank gameRank = gameRankMap.get(rank.getGid());
			if (null == gameRank) {
				gameRank = new TempRank();
				gameRank.game = new Game();
				gameRank.game.setGname(rank.getGname());
				gameRank.game.setId(rank.getGid());
				gameRankMap.put(gameRank.game.getId(), gameRank);

			}
			gameRank.voteMap.put(rank.getRank(), rank);
		}
		// rank1
		if (null != request.getParameter("type1")) {
			int type = Integer.valueOf(request.getParameter("type1"));
			if (type >= 0) {
				Set<TempRank> rank1 = new TreeSet<TempRank>();
				rank(gameRankMap, rank1, type);
				iRank = gameRankMap.size();
				for (TempRank rank : rank1) {
					rank.rank1 = iRank--;
				}
			}
		}
		// rank2
		if (null != request.getParameter("type2")) {
			int type = Integer.valueOf(request.getParameter("type2"));
			if (type >= 0) {
				Set<TempRank> rank2 = new TreeSet<TempRank>();
				rank(gameRankMap, rank2, type);
				iRank = gameRankMap.size();
				for (TempRank rank : rank2) {
					rank.rank2 = iRank--;
				}
			}
		}
		// rank3
		if (null != request.getParameter("type3")) {
			int type = Integer.valueOf(request.getParameter("type3"));
			if (type >= 0) {
				Set<TempRank> rank3 = new TreeSet<TempRank>();
				rank(gameRankMap, rank3, type);
				iRank = gameRankMap.size();
				for (TempRank rank : rank3) {
					rank.rank3 = iRank--;
				}
			}
		}
		// final rank
		Set<TempRank> rankFinal = new TreeSet<TempRank>();
		for (Entry<Long, TempRank> entry : gameRankMap.entrySet()) {
			TempRank gameRank = entry.getValue();
			gameRank.weight = gameRank.rank1;
			gameRank.weight += gameRank.rank2 * .8;
			gameRank.weight += gameRank.rank3 * .5;
			// System.out.printf("id:%d, name:%s, weight:%d, base:%d, r1:%d, r2:%d, r3:%d\n",
			// gameRank.game.getId(),
			// gameRank.game.getGname(), gameRank.weight, gameRank.base,
			// gameRank.rank1, gameRank.rank2,
			// gameRank.rank3);
			rankFinal.add(gameRank);
		}
		// return rank
		List<Game> rankList = new ArrayList<Game>(5);
		int i = 0;
		for (TempRank tmpRank : rankFinal) {
			if (i++ < 5) {
				rankList.add(tmpRank.game);
			} else {
				break;
			}
		}

		return JsonUtils.toJson(rankList);
	}

	private void rank(Map<Long, TempRank> gameRankMap, Set<TempRank> rankSet,
			int type) {
		for (Entry<Long, TempRank> entry : gameRankMap.entrySet()) {
			TempRank gameRank = entry.getValue();
			Rank vote = gameRank.voteMap.get(type);
			if (null != vote) {
				gameRank.weight = vote.getVotes();
				gameRank.base = vote.getBase();
			} else {
				gameRank.weight = 0;
				gameRank.base = 0;
			}
			// System.out.printf("type:%d,name:%s,vote:%d,base:%d\n",type,
			// gameRank.game.getGname(), gameRank.weight,
			// gameRank.base);
			rankSet.add(gameRank);
		}
	}

	class TempRank implements Comparable<TempRank> {
		public Game game;
		public Map<Integer, Rank> voteMap = new HashMap<Integer, Rank>();// type
																			// =>
																			// votes
		public int rank1;
		public int rank2;
		public int rank3;
		public int base;
		public int weight;

		@Override
		public int compareTo(TempRank o) {
			int diff = o.weight - weight;
			return diff == 0 ? (int) (base - o.base) : diff;
		}
	}

}