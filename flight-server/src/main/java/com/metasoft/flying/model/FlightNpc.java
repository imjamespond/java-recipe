package com.metasoft.flying.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.NpcFinishVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class FlightNpc extends Flight{
	private static final Logger logger = LoggerFactory.getLogger(FlightNpc.class);

	public FlightNpc(GameRoom room) {
		super(room);
		type = GeneralConstant.GTYPE_NPC;
	}
	
	@Override
	public void begin() {
		if (getPlayerNum() == 0) {
			logger.error("begin getPlayerNum is 0");
			return;
		}
		//设置npc 棋局
		for(ChessPlayer cp:chessPlayers){
			if(null==cp)
				continue;
			for(Chess chess:cp.getChesses()){
				chess.reset();
				chess.setState(ChessConstant.CHESS_FINISH);
			}
			for(int i=0;i<cp.getItems().length;i++){
				cp.getItems()[i]=1;
			}
			cp.getChesses()[0].setState(ChessConstant.CHESS_READY);
		}

		// 随机事件位置
		FlightHelper.randomCoord(this);

		beginNotify();
		nextTurn(0);
	}
	
	@Override
	public void broadcast(GeneralResponse resp){
		GameRoom gr = room.get();
		if(null==gr){
			return;
		}
		UserService us = SpringService.getBean(UserService.class);
		User user = us.getOnlineUserById(gr.getMasterId());
		if(user!=null){
			BaseConnection conn = user.getConn();
			if(conn!=null){
				conn.deliver(resp);
			}
		}
	}
	
	/**
	 * 结算
	 */
	@Override
	public void finishNotify(int pos) {
		NpcFinishVO vo = new NpcFinishVO();
		vo.setPos(pos);
		broadcast(GeneralResponse.newObject(vo));
		GameRoom gr = room.get();
		if(null!=gr){
			gr.setFlightNpc(null);
		}
	}
}
