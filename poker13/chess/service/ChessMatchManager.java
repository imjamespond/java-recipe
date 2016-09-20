package com.chitu.chess.service;

import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gecko.commons.timer.ScheduleManager;

import com.chitu.chess.data.StaticMatch;
import com.chitu.chess.model.ChessUtils;

@Service
public class ChessMatchManager {

	@Autowired
	private ScheduleManager scheduleManager;
	@Autowired
	private ChessRoomManager chessRoomManager;

	public int curDitrictId;
	
	public ChessMatchManager() {
	}

	@PostConstruct
	public void init() {
		Runnable t = new ChessTimer();
		scheduleManager.scheduleAtFixedRate(t, DateUtils.MILLIS_PER_SECOND * 30, DateUtils.MILLIS_PER_SECOND * 30);
	}

	
	public class ChessTimer implements Runnable {

		public ChessTimer() {
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ChessUtils.chessLog.info("=ChessMatchManager=");				
			Iterator<Entry<Integer, StaticMatch>>  it = StaticMatch.getAll().entrySet().iterator();
			while (it.hasNext()) 
			{
				Entry<Integer, StaticMatch> en = it.next();
				StaticMatch sm = en.getValue();
				
				chessRoomManager.constructDistrictMatchId(sm.getId());//不存在建立
				chessRoomManager.updateMatch(sm.getId());//
				int state = chessRoomManager.getMatchState(sm.getId(), 0);
				if( state == 2 || state == 3){//开始
					curDitrictId  =  sm.getId();
				}
				//ChessUtils.chessLog.info("ChessMatchManager:");
			}	
		}

	}
}
