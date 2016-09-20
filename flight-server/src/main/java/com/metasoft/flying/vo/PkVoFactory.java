package com.metasoft.flying.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.metasoft.flying.controller.DressController;
import com.metasoft.flying.model.PkArena;
import com.metasoft.flying.model.PkPlayer;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ArenaConstant;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.general.GeneralResponse;

public class PkVoFactory {

	public static PkArenaStartVO getPkArenaStartVO(PkArena arena) {
		PkArenaStartVO vo = new PkArenaStartVO();
		vo.setId(0l);
		vo.setName(arena.getName());
		List<PkPlayerVO> voList = new ArrayList<PkPlayerVO>(8);
		PkPlayer[] players = arena.getPkPlayers();
		for (int i = 0; i < players.length; ++i) {
			if (null != players[i]) {
				if (players[i].getUserId() > 0l) {
					//copy data
					PkPlayerVO pvo = getPkPlayerVO(players[i]);
					voList.add(pvo);
				}
			}
		}
		vo.setPkType(arena.getPkType());
		vo.setPlayers(voList);
		vo.setPkTime(ArenaConstant.AREAN_LENGTH);
		return vo;
	}
	public static PkArenaInfoVO getPkArenaInfoVO(PkArena arena) {
		PkArenaInfoVO vo = new PkArenaInfoVO();
		vo.setId(0l);
		vo.setName(arena.getName());
		vo.setState(arena.getState());
		vo.setPkTime((int)(arena.getTime()+ArenaConstant.AREAN_LENGTH-System.currentTimeMillis()));
		List<PkPlayerVO> voList = new ArrayList<PkPlayerVO>(8);
		PkPlayer[] players = arena.getPkPlayers();
		for (int i = 0; i < players.length; ++i) {
			if (null != players[i]) {
				if (players[i].getUserId() > 0l) {
					//copy data
					PkPlayerVO pvo = getPkPlayerVO(players[i]);
					voList.add(pvo);
				}
			}
		}
		vo.setPkType(arena.getPkType());
		vo.setPlayers(voList);
		return vo;
	}
	public static PkMatchingVO getPkMatchingVO(PkArena arena) {
		PkMatchingVO vo = new PkMatchingVO();
		vo.setId(0l);
		vo.setName(arena.getName());
		vo.setState(arena.getState());
		return vo;
	}
	private static PkPlayerVO getPkPlayerVO(PkPlayer pkPlayer) {
		PkPlayerVO vo = new PkPlayerVO();
		BeanUtils.copyProperties(pkPlayer, vo);	
		User user = pkPlayer.getUser();
		if(user!=null){
			vo.setName(user.getUserPersist().getNickname());
		}
		//计算剩下时间
		vo.setRebirth(System.currentTimeMillis()-vo.getRebirth());
		vo.setAttack(pkPlayer.getHurt(0f));
		return vo;
	}

	public static PkGoVO getPkGoVO(PkGoRequest req) {
		PkGoVO vo = new PkGoVO();
		vo.setX(req.getX());
		vo.setY(req.getY());
		vo.setPos(req.getPos());
		return vo;
	}

	public static PkFireVO getPkFireVO(PkFireRequest req) {
		PkFireVO vo = new PkFireVO();
		vo.setPos(req.getPos());
		vo.setType(req.getType());
		vo.setTarget(req.getTarget());
		return vo;
	}

	public static PkHurtVO getPkHurtVO(PkHurtRequest req,PkPlayer player, PkPlayer target, int hurt) {
		PkHurtVO vo = new PkHurtVO();
		vo.setPos(req.getPos());
		vo.setTarget(req.getTarget());
		vo.setHp(target.getHp());
		vo.setHurt(hurt);
		vo.setScore(player.getScore());
		return vo;
	}	
	public static PkRebirthVO getPkRebirthVO(PkPlayer player) {
		PkRebirthVO vo = new PkRebirthVO();
		vo.setPlayer(PkVoFactory.getPkPlayerVO(player));
		return vo;
	}
	public static PkArenaEndVO getPkArenaEndVO(PkArena pkArena, PkPlayer[] players) {
		PkArenaEndVO vo = new PkArenaEndVO();
		vo.setInfo(PkVoFactory.getPkArenaInfoVO(pkArena));
		
		//计分
		int team = 0;
		int teamScore = 0;
		HashMap<Integer, Integer> teamMap = new HashMap<Integer, Integer>();
		for (PkPlayer player:players) {
			if (null != player) {
				// 是否有玩家
				if (player.getUserId() > 0l) {
					if(teamMap.containsKey(player.getTeam())){
						teamMap.put(player.getTeam(), player.getScore() + teamMap.get(player.getTeam()));
					}else{
						teamMap.put(player.getTeam(), player.getScore());
					}
				}
			}
		}
		Set<Entry<Integer, Integer>> set = teamMap.entrySet();
		for(Entry<Integer, Integer> e:set){
			if(teamScore < e.getValue()){
				team = e.getKey();
				teamScore = e.getValue();
			}else if(teamScore == e.getValue()){//平局
				team = 0;
				teamScore = 0;
			}
		}
		List<PkScoreVO> listVO = new ArrayList<PkScoreVO>(8);
		for (PkPlayer player:players) {
			if (null != player) {
				// 是否有玩家
				if (player.getUserId() == 0l ) {
					continue;
				} else {
					UserService userService = SpringService.getBean(UserService.class);

					int score = 1;				
					try {
						User user = userService.getAnyUserById(player.getUserId());
						if( team > 0){
							// 查询飞机
							DressController ic = SpringService.getBean(DressController.class);
							score += ic.checkPlaneIsPutOn(user);					
							// 胜负
							if(team == player.getTeam()){
								score *= ChessConstant.SCORE_WIN;
	
								LocalizationService localService = SpringService.getBean(LocalizationService.class);
								String msg = localService.getLocalString("chess.victory",
										new String[] { String.valueOf(score) });
								if (user.getConn() != null) {
									user.getConn().deliver(GeneralResponse.newObject(new ChatVO(msg)));
								}						
							}else{
								score *= ChessConstant.SCORE_LOSE;
	
								LocalizationService localService = SpringService.getBean(LocalizationService.class);
								String msg = localService.getLocalString("chess.lose",
										new String[] { String.valueOf(score) });
								if (user.getConn() != null) {
									user.getConn().deliver(GeneralResponse.newObject(new ChatVO(msg)));
								}							
							}
						}
						//平局
						else{
							score = 0;
						}
						user.addScore(score);
						// 财富通知
						if (user.getConn() != null) {
							user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
						}

						PkScoreVO svo = new PkScoreVO(player.getUserId(), player.getPos(), score);
						svo.setNickname(user.getUserPersist().getNickname());
						listVO.add(svo);
					} catch (GeneralException e) {
						e.printStackTrace();
					}
				}
			}//if
			
		}//for
		vo.setScoreList(listVO);
		return vo;
	}
	
	public static PkArenaEndVO getPkArenaEndVO2() {
		PkArenaEndVO vo = new PkArenaEndVO();
		PkArenaInfoVO infoVo = new PkArenaInfoVO();
		infoVo.setState(ArenaConstant.INTERRUPT);
		vo.setInfo(infoVo);
		return vo;
	}	
	
	public static PkEmperorEndVO getPkEmperorEndVO(int state, PkPlayer[] players) {
		PkEmperorEndVO vo = new PkEmperorEndVO();
		vo.setState(state);
		
		List<PkScoreVO> listVO = new ArrayList<PkScoreVO>(8);
		for (PkPlayer player:players) {
			if (null != player) {
				// 是否有玩家
				if (player.getUserId() == 0l) {
					continue;
				} else {
					UserService userService = SpringService.getBean(UserService.class);

					int score = 1;
					try {
						User user = userService.getAnyUserById(player.getUserId());
						// 查询飞机
						DressController ic = SpringService.getBean(DressController.class);
						score += ic.checkPlaneIsPutOn(user);

						// 胜负
						int win = 0;
						if (state == 1) {
							if(player.getTeam() == 1 || player.getTeam() == 2){
								win = 1;
							}
						} else if(state == 2){
							if(player.getTeam() == 3 ){
								win = 1;
							}
						} else if(state == 4){
							if(player.getTeam() == 4 ){
								win = 1;
							}
						}
						
						//
						if(win>0){
							score *= ChessConstant.SCORE_WIN;

							LocalizationService localService = SpringService.getBean(LocalizationService.class);
							String msg = localService.getLocalString("chess.victory",
									new String[] { String.valueOf(score) });
							if (user.getConn() != null) {
								user.getConn().deliver(GeneralResponse.newObject(new ChatVO(msg)));
							}						
						}else{
							score *= ChessConstant.SCORE_LOSE;

							LocalizationService localService = SpringService.getBean(LocalizationService.class);
							String msg = localService.getLocalString("chess.lose",
									new String[] { String.valueOf(score) });
							if (user.getConn() != null) {
								user.getConn().deliver(GeneralResponse.newObject(new ChatVO(msg)));
							}							
						}
						
						user.addScore(score);
						// 财富通知
						if (user.getConn() != null) {
							user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
						}

						PkScoreVO svo = new PkScoreVO(player.getUserId(), player.getPos(), score);
						svo.setNickname(user.getUserPersist().getNickname());
						listVO.add(svo);
					} catch (GeneralException e) {
						e.printStackTrace();
					}
				}
			}//if
			
		}//for
		vo.setScoreList(listVO);
		return vo;
	}
}
