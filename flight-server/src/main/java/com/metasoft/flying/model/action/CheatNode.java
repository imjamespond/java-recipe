package com.metasoft.flying.model.action;

import java.util.ArrayDeque;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.vo.ChessFlightVO;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * 行为节点,开挂,吃前面三格飞机
 * 
 * @author james
 *
 */
public class CheatNode extends ActionNode {

	@Override
	public boolean act(Flight flight) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return false;
		}
		
		if((flight.degree & ActionNode.kCheat)>0){
		//是否有电磁干扰
		boolean fogCheck = FlightHelper.fogCheck(flight.getChessPlayers(), flight.getCurrentPos());	
		for (Chess chess : cp.getChesses()) {
			//可走棋子
			if ((ChessConstant.CHESS_FLIGHT & chess.getState()) > 0) {
				//不受电磁干扰 或 受干扰但在跑道中
				if( !fogCheck || (fogCheck && chess.getJourney()>ChessConstant.JOURNEY_JUMP)){
					
					int dice = 0;
					
					//检测前面三格飞机
					ChessPlayer[] chessPlayers = flight.getChessPlayers();
					int pos = flight.getPos();
					// step by step
					for(int i=1; i<4; i++){
						int detect = Chess.getCoord(chess.getJourney()+i, chess.getPos());
						for (int j = 0; j < chessPlayers.length; ++j) {
							ChessPlayer cplayer = chessPlayers[j];
							if(cplayer!=null && cplayer.getUserId()>0l && cplayer.getPos()!=pos && cplayer.getNpc()==0){
								for(Chess enemy:cplayer.getChesses()){
									if(enemy.getCurCoord()==detect && (ChessConstant.CHESS_FLIGHT & enemy.getState()) > 0){
										dice = i;
										break;
									}
								}
							}
						}
					}
					
					if(dice>0){
						flight.setDice(dice);
						// 广播扔色子
						flight.broadcast(GeneralResponse.newObject(VoFactory.getChessDiceVO(flight)));			
						
						// 找出行程相同的
						ArrayDeque<Chess> chessGoList = flight.getChessByJourney(cp.getChesses(), chess.getJourney());
						ChessFlightVO vo = new ChessFlightVO();
						vo.setGoList(flight.goCalculate( chessGoList));
						vo.setPos(flight.getPos());	
						// 广播自动走棋计算
						flight.broadcast(GeneralResponse.newObject(vo));
						flight.setWaitAnim();
						return true;// 只走一个棋
					}	
				}
			}
		}
		}

		if (null != next) {
			return next.act(flight);
		}
		return false;
	}
}
