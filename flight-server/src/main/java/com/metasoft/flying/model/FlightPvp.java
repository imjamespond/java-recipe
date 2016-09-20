package com.metasoft.flying.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.vo.general.GeneralResponse;

public class FlightPvp extends Flight{
	private static final Logger logger = LoggerFactory.getLogger(FlightPvp.class);
	
	public int gold;

	public FlightPvp(GameRoom room) {
		super(room);
		type = GeneralConstant.GTYPE_NORMAL;
	}
	
	@Override
	public void begin() {
		if (getPlayerNum() == 0) {
			logger.error("begin getPlayerNum is 0");
			return;
		}

		// 随机事件位置
		FlightHelper.randomCoord(this);

		beginNotify();
		nextTurn(0);
	}
	
	/**
	 * 结算
	 */
	@Override
	public void finishNotify(int pos) {
		// 结束vo
		broadcast(GeneralResponse.newObject(FlightHelper.getChessFinishVO2(chessPlayers, chessPlayers[pos].getUserId(), gold)));
		
		GameRoom gr = room.get();
		if(null!=gr){
			gr.setFlightPvp(null);
		}
		
		gold = 0;
	}
}
