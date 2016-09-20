package com.chitu.chess.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gecko.commons.model.Pageable;
import cn.gecko.commons.timer.ScheduleManager;

import com.chitu.chess.model.ChessUtils;
import com.chitu.chess.model.PersistChessPlayer;
import com.chitu.chess.msg.ChessPlayerRankListDto;
import com.chitu.chess.msg.ChessPlayerRankMDto;
import com.chitu.chess.msg.ChessPlayerRankPDto;

@Service
public class ChessPlayerRankManager {

	public ChessPlayerRankListDto rankList;
	

	@Autowired
	private ScheduleManager scheduleManager;

	public ChessPlayerRankManager() {
	}

	@PostConstruct
	public void init() {
		rankList = new ChessPlayerRankListDto();
		Runnable t = new ChessPlayerRankTimer();
		scheduleManager.scheduleAtFixedRate(t, DateUtils.MILLIS_PER_SECOND * 60, DateUtils.MILLIS_PER_SECOND * 3333);
	}

	
	public class ChessPlayerRankTimer implements Runnable {

		public ChessPlayerRankTimer() {
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ChessUtils.chessLog.info("=ChessPlayerRankTimer=");
			
			List<ChessPlayerRankPDto> p = new ArrayList<ChessPlayerRankPDto>();
			List<ChessPlayerRankMDto> m = new ArrayList<ChessPlayerRankMDto>();
			
			Pageable<PersistChessPlayer> rank = PersistChessPlayer.find(0,50," order by point desc");//var page is unavailable!
			List<PersistChessPlayer> list = rank.getElements();
			Iterator<PersistChessPlayer> itr = list.iterator();
			while (itr.hasNext()) {
				PersistChessPlayer pc = itr.next();
				ChessPlayerRankPDto tmp = new ChessPlayerRankPDto();
				tmp.setAccountId(pc.getAccountId());
				tmp.setNickname(pc.getNickname());
				tmp.setPoint(pc.getPoint());
				tmp.setTitle(ChessUtils.point2Title(pc.getPoint()));
				tmp.setGender(pc.getGender());
				tmp.setAvatar(pc.getAvatar());
				p.add(tmp);
				//ChessUtils.chessLog.info("=ChessPlayerRankPDto=:"+pc.getNickname()+'_'+pc.getPoint());
			}
	
			rank = PersistChessPlayer.find(0,50," order by money desc");//var page is unavailable!
			list = rank.getElements();
			itr = list.iterator();
			while (itr.hasNext()) {
				PersistChessPlayer pc = itr.next();
				ChessPlayerRankMDto tmp = new ChessPlayerRankMDto();
				tmp.setAccountId(pc.getAccountId());
				tmp.setNickname(pc.getNickname());
				tmp.setMoney(pc.getMoney());
				tmp.setTitle(ChessUtils.point2Title(pc.getPoint()));
				tmp.setGender(pc.getGender());
				tmp.setAvatar(pc.getAvatar());
				m.add(tmp);
				//ChessUtils.chessLog.info("=ChessPlayerRankMDto=:"+pc.getNickname()+'_'+pc.getMoney());
			}
			
			ChessPlayerRankPDto[] arrP = new ChessPlayerRankPDto[p.size()];
			ChessPlayerRankMDto[] arrM = new ChessPlayerRankMDto[m.size()];
			p.toArray(arrP);
			m.toArray(arrM);
			rankList.setPointRank(arrP);
			rankList.setMoneyRank(arrM);
			
		}

	}
}
