package com.metasoft.flying.model.action;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.vo.ChessReadyVO;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * 行为节点,扔5,6(如果待飞区有一架飞机不出)出飞机
 * 
 * @author james
 *
 */
public class TakeOffNode extends ActionNode {

	@Override
	public boolean act(Flight flight) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return false;
		}
		
		if (flight.getDice() >= 5 && cp.getItemPos()!=ChessConstant.ITEM_ENHANCE && cp.getItemPos()!=ChessConstant.ITEM_RELAY) {
			//if((flight.degree & ActionNode.kDice6)==0){
				//return BasisNode.act(flight);
			//}
			
			int count = 0;
			for (Chess chess : cp.getChesses()) {
				if(chess.getState() == ChessConstant.CHESS_READY){
					count++;
				}
			}

			for (Chess chess : cp.getChesses()) {
				if (chess.getState() == 0&&count<2) {
					chess.setState(ChessConstant.CHESS_READY);

					// 广播自动走棋计算
					ChessReadyVO vo = VoFactory.getChessReadyVO(chess);
					flight.broadcast(GeneralResponse.newObject(vo));
					return true;// 只走一个棋
				}
			}
		}

		if (null != next) {
			return next.act(flight);
		}
		return false;
	}
}