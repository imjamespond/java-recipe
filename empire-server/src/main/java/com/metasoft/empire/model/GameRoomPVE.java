package com.metasoft.empire.model;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.service.UserService;
import com.metasoft.empire.service.common.SpringService;
import com.metasoft.empire.vo.GameEndVO;
import com.metasoft.empire.vo.GameExpiredVO;

public class GameRoomPVE extends GameRoom{
	private static final Logger log = LoggerFactory.getLogger(GameRoomPVE.class);
	
	static{
		log.debug(GameRoomPVE.class.toString());
	}
	
	@Override
	public void swap(long uid, int swap){
		if(!isBegin()){
			return;
		}
		
		if(swap>0){
			player0.setSwap(swap,turn);
			player1.autoSwap();
		}else{
			player0.vo.decreaseHp(100);
			gameBroadcast(GeneralResponse.newJsonObj(new GameExpiredVO(uid, player0.vo.getHp())));
			if(checkEnd()){
				end();
			}
			return;
		}
		
		if(player0.turn==turn){
			nextTurn();
			combat();
			
			if(checkEnd()){
				end();
			}
		}
	}
	@Override
	public void end(){
		state=STATE_END;
		
		GameEndVO vo = new GameEndVO();
		
		if(player0.vo.getHp()<=0&&player1.vo.getHp()<=0){
			
		}else if(player0.vo.getHp()<=0){
			vo.setUid(player1.vo.getUid());
		}else if(player1.vo.getHp()<=0){
			vo.setUid(player0.vo.getUid());
		}
		gameBroadcast(GeneralResponse.newJsonObj(vo));
		
		//unbind user's gameroom  
		UserService us = SpringService.getBean(UserService.class);
		User user=null;
		try {
			user = us.getAnyUserById(player0.vo.getUid());
			if(null!=user){
				user.gameRoom=null;
				Arrays.fill(user.roles, 0);
			}
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}
}
