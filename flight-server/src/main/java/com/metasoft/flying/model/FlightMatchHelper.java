package com.metasoft.flying.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.UserRankService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.ChessFinishVO;
import com.metasoft.flying.vo.ChessInfoVO;
import com.metasoft.flying.vo.ChessScoreVO;
import com.metasoft.flying.vo.MatchFinishVO;
import com.metasoft.flying.vo.MatchInfoVO;
import com.metasoft.flying.vo.MatchPlayerVO;

public class FlightMatchHelper {
	private static final Logger logger = LoggerFactory.getLogger(FlightMatchHelper.class);
	
	public static MatchInfoVO getMatchInfoVO(FlightMatch match) {
		MatchInfoVO vo = new MatchInfoVO();
		vo.setRemain(FlightMatch.MATCH_LENGTH - System.currentTimeMillis() + match.getCommence());
		ChessInfoVO info = VoFactory.getChessInfoVO(match);
		vo.setInfo(info);
		return vo;
	}

	public static MatchFinishVO getMatchFinishVO(FlightMatch match, long elapsed) {
		ChessPlayer[] chessPlayers = match.chessPlayers;
		UserService us = SpringService.getBean(UserService.class);
		UserRankService urs = SpringService.getBean(UserRankService.class);
		MatchService ms = SpringService.getBean(MatchService.class);
		MatchFinishVO finishVO = new MatchFinishVO();

		Set<MatchPlayer> set = new TreeSet<MatchPlayer>(new Comparator<MatchPlayer>(){
			@Override
			public int compare(MatchPlayer o1, MatchPlayer o2) {
				//由大到小
				if(o1.finish<o2.finish){
					return 1;
				}else if(o1.finish == o2.finish){
					if(o1.standby<o2.standby){
						return 1;
					}else if(o1.standby == o2.standby){
						if(o1.jouney<o2.jouney){
							return 1;
						}
					}
				}
				return -1;
			}
		});
		int i = 0;
		for ( ; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && chessPlayers[i].getUserId() != 0l) {
				ChessPlayer cp = chessPlayers[i];
				
				MatchPlayer mp = match.getMatchPlayer(i);
				if(null != mp){
					mp.setUserid(cp.getUserId());
					//分数4,2,1,0
					//回场飞机 
					mp.finish = cp.finishCount();
					//出场飞机
					mp.standby = cp.standbyCount();
					//离终点最近
					mp.jouney = cp.jouneyMax();
					//完成时间
					mp.elapsed = elapsed;
					//位置
					mp.pos = cp.getPos();
					set.add(mp);
					logger.debug("finish:{}, standby:{}, jouney:{}",mp.finish ,mp.standby ,mp.jouney );
				}
			}
		}
		
		//加分
		i = 1;
		ChessFinishVO cfVO = new ChessFinishVO();
		List<ChessScoreVO> listVO = new ArrayList<ChessScoreVO>(4);
		for (MatchPlayer mp : set){		
			int score = 0;
			if(1 == i){
				score = 4;
				cfVO.setPos(mp.pos);
			}else if(2 == i){
				score = 2;
			}else if(3 == i){
				score = 1;
			}
			mp.rank = i;
			//排行
			try {
				//持久化
				User user = us.getAnyUserById(mp.getUserid());
				UserRankPersist urank = user.getUserRankPersistByDay();
				urank.setMatch(urank.getMatch()+score);
				urank.setElapsed(mp.elapsed+urank.getElapsed());
				urs.update(urank);
				//每小时排行
				MatchPlayerVO mvo = ms.matchResult.get(mp.getUserid());
				if(null==mvo){
					mvo = new MatchPlayerVO();
					ms.matchResult.put(mp.getUserid(), mvo);
				}
				mvo.setElapsed(mvo.getElapsed()+elapsed);
				mvo.setScore(mvo.getScore()+score);
				mvo.setUserId(mp.getUserid());
				mvo.setUserName(user.getUserPersist().getNickname());
				//分数
				ChessScoreVO vo = new ChessScoreVO(mp.getUserid(), i, score);
				vo.setNickname(user.getUserPersist().getNickname());
				vo.setPos(mp.pos);
				listVO.add(vo);
			} catch (GeneralException e) {
				e.printStackTrace();
			} finally{
				
			}
			
			i++;
		}
		cfVO.setScoreList(listVO);
		finishVO.setFinish(cfVO);
		
		return finishVO;
	}
	
}
