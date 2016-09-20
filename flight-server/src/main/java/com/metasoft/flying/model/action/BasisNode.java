package com.metasoft.flying.model.action;

import java.util.ArrayDeque;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.vo.ChessFlightVO;
import com.metasoft.flying.vo.ChessReadyVO;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * 行为节点,基本走棋
 * 
 * @author james
 *
 */
public class BasisNode {

	public static boolean act(Flight flight) {
		// 自动走棋
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return false;
		}

		cp.setItemPos(-1);

		//是否有迷雾
		boolean fogCheck = FlightHelper.fogCheck(flight.chessPlayers, flight.getCurrentPos());
		// 找出可走的棋
		for (Chess chess : cp.getChesses()){
			if (chess.getState() == 0) {
				if (flight.getDice() >= 5) {
					chess.setState(ChessConstant.CHESS_READY);

					// 广播自动走棋计算
					ChessReadyVO vo = VoFactory.getChessReadyVO(chess);
					flight.broadcast(GeneralResponse.newObject(vo));
					break;// 只走一个棋
				}
			} else if (chess.isReady()) {
				ArrayDeque<Chess> chessGoList = new ArrayDeque<Chess>();
				chessGoList.add(chess);

				ChessFlightVO vo = new ChessFlightVO();
				//ArrayDeque<Chess> chessList = chessGoList.clone();
				vo.setGoList(flight.goCalculate( chessGoList));
				vo.setPos(flight.getPos());

				// 广播走棋计算
				flight.broadcast(GeneralResponse.newObject(vo));

				break;// 只走一个棋
			} else if (chess.isFlying()) {
				if( !fogCheck || (fogCheck && !chess.isOutside())){
				// 找出行程相同的
				ArrayDeque<Chess> chessGoList = flight.getChessByJourney(cp.getChesses(), chess.getJourney());

				ChessFlightVO vo = new ChessFlightVO();
				//ArrayDeque<Chess> chessList = chessGoList.clone();
				vo.setGoList(flight.goCalculate( chessGoList));
				vo.setPos(flight.getPos());
			
				// 广播自动走棋计算
				flight.broadcast(GeneralResponse.newObject(vo));
				
				//sign|=1;//标记
				break;// 只走一个棋
				}
			}
		}
		return true;
	}
}
