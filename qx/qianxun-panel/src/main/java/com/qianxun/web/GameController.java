package com.qianxun.web;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianxun.service.RankScheduleService;
import com.test.qianxun.model.GameEx;
import com.test.qianxun.model.GameVote;
import com.test.qianxun.model.Rank;
import com.test.qianxun.service.GameExService;
import com.test.qianxun.service.GameVoteService;
import com.test.qianxun.service.RankService;

@Controller
@RequestMapping(value = "/game")
public class GameController {
	@Autowired
	private GameExService gameService;
	@Autowired
	private GameVoteService gameVoteService;
	@Autowired
	private RankService rankService;
	@Autowired
	private RankScheduleService rsService;
	private int limit = 50;


	@RequestMapping(value = "/updaterank", method = RequestMethod.POST)
	@ResponseBody
	public String updateRank(){
		rsService.rankWithoutTrend();
		return "ok";
	}
	@RequestMapping(value = "/updatetrend", method = RequestMethod.POST)
	@ResponseBody
	public String updatetrend(){
		rsService.rank();
		return "ok";
	}		
	@RequestMapping(value = "/updatevote", method = RequestMethod.POST)
	@ResponseBody
	public String updatevote(@RequestParam("id") long id
			, @RequestParam("vote") int vote
			, @RequestParam("trend") int trend
			, @RequestParam("type") int type) {
		GameEx game = gameService.get(id);
		if(null==game){
			return "redirect:/game/list";
		}
		GameVote gv = gameVoteService.get(id);
		if(null == gv){
			gv = new GameVote(id);
			gameVoteService.save(id, gv);
		}
		gameVoteService.updateVote(id, type, vote, trend);
		return "ok";
	}
	
	@RequestMapping("/remove/{id}")
	public String remove(@PathVariable long id) {

		gameService.delete(id);
		return "redirect:/game/list";
	}
	
	
	//previous code
	
	
	@RequestMapping("/list")
	public String list(Model model) {
		return list(1, model);
	}

	@RequestMapping("/list/{number}")
	public String list(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<GameEx> gameList = gameService.listAll(page);
		model.addAttribute("gameList", gameList);
		model.addAttribute("page", page);
		return "game/list";
	}

	@RequestMapping("/updatestate/{id}")
	public String updateState(@PathVariable long id) {
		GameEx game = gameService.get(id);
		if (game.getState() == 1) {
			game.setState(0);
		} else {
			game.setState(1);
		}
		gameService.update(game);
		List<Rank> rankList = rankService.listByGid(id);
		for (Rank rank : rankList) {
			if (rank.getState() == 1) {
				rank.setState(0);
			} else {
				rank.setState(1);
			}
			rankService.update(rank);
		}
		return "redirect:/game/list";
	}
	

	@RequestMapping("/disableall")
	public String disable() {
		List<GameEx> gameList = gameService.listAllByState(1);
		for (GameEx game : gameList) {
			game.setState(0);
			gameService.update(game);
		}
		List<Rank> rankList = rankService.listByState(1);
		for (Rank rank : rankList) {
			rank.setState(0);
			rankService.update(rank);
		}
		return "redirect:/game/list";
	}

	@RequestMapping("/create")
	public String create() {
		return "game/save";
	}

	@RequestMapping("/rank")
	public String rank() {
		return "game/rank";
	}

	@RequestMapping("/setRank/{type}/{rank}")
	public String setRank(@PathVariable int type, @PathVariable int rank,
			Model model) {
		List<Rank> rankList = rankService.listByTypeAndRank(type, rank);
		model.addAttribute("type", type);
		model.addAttribute("rank", rank);
		model.addAttribute("rankList", rankList);
		return "game/setRank";
	}

	@RequestMapping("/changeRank")
	@ResponseBody
	public String changeRank(@RequestParam("type") int type,
			@RequestParam("rank") int rank,
			@RequestParam("rankList") String rankList) {
		String[] games = rankList.split(",");
		int i = 0;
		for (String id : games) {
			Rank r = rankService.findByTypeAndRank(Long.parseLong(id), type,
					rank);
			r.setBase(i);
			r.setVotes(0);
			rankService.update(r);
			i++;
		}
		return "1";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(GameEx game) {
		game.setState(1);
		long gid = gameService.save(game);
		int base = gameService.countByType(game.getType());
		for (int j = 0; j < 8; j++) {
			Rank rank = new Rank();
			rank.setGid(gid);
			rank.setGname(game.getGname());
			rank.setType(game.getType());
			rank.setRank(j);
			rank.setBase(base);
			rank.setState(1);
			rankService.save(rank);
		}
		return "redirect:/game/create";
	}

}