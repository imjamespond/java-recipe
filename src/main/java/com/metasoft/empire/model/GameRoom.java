package com.metasoft.empire.model;

import java.util.Arrays;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.net.BaseConnection;
import com.metasoft.empire.service.UserService;
import com.metasoft.empire.service.common.SpringService;
import com.metasoft.empire.vo.GameEndVO;
import com.metasoft.empire.vo.GameExpiredVO;
import com.metasoft.empire.vo.UpdateVO;

public class GameRoom {
	private static final Logger log = LoggerFactory.getLogger(GameRoom.class);
	
	public GamePlayer player0 = new GamePlayer();
	public GamePlayer player1 = new GamePlayer();
	public int turn;
	public int type;//1为pve
	public int map;
	public long deadline;
	
	public static final long WAIT_TIME = 60*DateUtils.MILLIS_PER_SECOND;
	public static final int MAX_TURN = 8;
	
	public int state;
	public static final int STATE_BEGIN = 1;
	public static final int STATE_END = 2;
	public static final int STATE_WAIT = 4;
	
	public void reset(){
		turn = 0;
		state = 0;
		deadline = 0l;
		player0.reset();
		player1.reset();
		player0.vo.randomSwap();
		player1.vo.randomSwap();
	}
	
	public void swap(long uid, int swap){
		if(!isBegin()){
			return;
		}
		
		GamePlayer gp = getPlayer(uid);
		if(null!=gp){
			if(swap>=0)
				gp.setSwap(swap,turn);
			else{
				gp.vo.decreaseHp(100);
				gameBroadcast(GeneralResponse.newJsonObj(new GameExpiredVO(uid, gp.vo.getHp())));
				if(checkEnd()){
					end();
				}
				return;
			}
		}
		
		if(player0.turn==turn&&player1.turn==turn/**/){
			nextTurn();
			combat();
			
		}
		if(checkEnd()){
			end();
		}
	}
	
	public void autoSwap(){
		if(player0.turn!=turn){
			player0.autoSwap();
		}
		if(player1.turn!=turn){
			player1.autoSwap();
		}

		nextTurn();
		combat();
		
		
		if(checkEnd()){
			end();
			return;
		}
	}
	
	public GamePlayer getPlayer(long uid){
		if(uid == player0.vo.getUid())
			return player0;
		else if(uid == player1.vo.getUid())
			return player1;
		return null;
	}
	
	public void combat(){
		gameBroadcast(GameCombat.combat(this));
	}
	
	public boolean checkEnd(){
		if(player0.vo.getHp()<=0||player1.vo.getHp()<=0){
			return true;
		}
		if(turn >= MAX_TURN){
			return true;
		}
		return false;
	}
	
	public void begin(){
		reset();
		
		state|=STATE_BEGIN;
		
		nextTurnDeanline();
		//turn=0;//must be 0
	}
	
	public boolean isBegin(){
		return (state&STATE_BEGIN)>0;
	}
	public boolean isEnd(){
		return (state&STATE_END)>0;
	}
	
	public void end(){
		state=STATE_END;
		
		if(player0.vo.getHp()<=0&&player1.vo.getHp()<=0){
			
		}else if(player0.vo.getHp()<=0){
			settle(player1.vo.getUid(), player0.vo.getUid());
		}else if(player1.vo.getHp()<=0){
			settle(player0.vo.getUid(), player1.vo.getUid());
		}
		
		//unbind user's gameroom  
		UserService us = SpringService.getBean(UserService.class);
		User user=null;
		try {
			user = us.getAnyUserById(player0.vo.getUid());
			if(null!=user){
				user.gameRoom=null;
				Arrays.fill(user.roles, 0);
			}
			user = us.getAnyUserById(player1.vo.getUid());
			if(null!=user){
				user.gameRoom=null;
				Arrays.fill(user.roles, 0);
			}
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}
	
	private void settle(long uid0, long uid1){
		GameEndVO vo = new GameEndVO();
		GeneralResponse.newJsonObj(vo);
		
		UserService us = SpringService.getBean(UserService.class);	
		try{
			User winner = us.getAnyUserById(uid0);
			User loser = us.getAnyUserById(uid1);
			
			winner.addScore(10);
			loser.decreaseScore(5);
			
			UpdateVO updatevo = new UpdateVO();
			BaseConnection<?> winnerconn = winner.getConn();
			if(null!=winnerconn){
				updatevo.setScore(winner.getScore());
				vo.setScore(10);
				vo.setUid(uid0);
				winnerconn.deliver(GeneralResponse.newJsonObj(updatevo ));
				winnerconn.deliver(GeneralResponse.newJsonObj(vo ));
			}
			BaseConnection<?> loserconn = loser.getConn();
			if(null!=loserconn){
				updatevo.setScore(loser.getScore());
				vo.setScore(-5);
				loserconn.deliver(GeneralResponse.newJsonObj(updatevo ));
				loserconn.deliver(GeneralResponse.newJsonObj(vo ));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean check(){
		long now = System.currentTimeMillis();
		if(now > deadline){
			autoSwap();
		}
		if(isEnd()){
			return true;
		}else if(checkEnd()){
			end();
			return true;
		}
		return false;
	}
	
	protected void nextTurn(){
		turn++;
		nextTurnDeanline();
	}
	private void nextTurnDeanline(){
		deadline=System.currentTimeMillis()+WAIT_TIME;
	}
	
	protected void gameBroadcast(Object vo){
		log.debug("gameBroadcast: {}\n",(String)vo);
		UserService us = SpringService.getBean(UserService.class);
		User u0 = us.getOnlineUserById(player0.vo.getUid());
		if(null!=u0)u0.deliver(vo);
		//FIXME
		User u1 = us.getOnlineUserById(player1.vo.getUid());
		if(null!=u1)u1.deliver(vo);
	}
}
