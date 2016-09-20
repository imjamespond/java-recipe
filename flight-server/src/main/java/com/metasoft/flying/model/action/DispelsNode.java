package com.metasoft.flying.model.action;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.vo.ChessItemVO;

/**
 * 行为节点,使用驱散
	前6格中，如出现龙卷风， 则判断: 
	龙卷风 如未到距离终点一半路程，则不使用照明导弹；
	如果龙卷风 在后半段路程，并且拥有照明导弹，则使用照明导弹
 * 
 * @author james
 *
 */
public class DispelsNode extends ActionNode {

	@Override
	public boolean act(Flight flight) {
		int pos = flight.getPos();
		ChessPlayer cp = flight.getChessPlayerByPos(pos);
		if (null == cp) {
			return false;
		}
		
		if((flight.degree & ActionNode.kItem)>0){
		int tornado = -1;
		for (ChessItemVO item : flight.items) {
			// 龙卷风
			if (item.getItemId() == ChessConstant.INCIDENT_TORNADO) {
				tornado = item.getCoord();
			}
		}
		
		int[] items = cp.getItems();
		if (items[ChessConstant.ITEM_DISPEL] > 0) {
			for (Chess chess : cp.getChesses()) {
				if ((ChessConstant.CHESS_FLIGHT & chess.getState()) > 0) {
					for(int i=1; i<10; i++){
						if ( chess.getJourney()+i <= ChessConstant.JOURNEY_JUMP) {
							if(Chess.getCoord(chess.getJourney()+i, pos) == tornado){
								FlightHelper.useDispel(flight);
								break;
							}
						}
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
