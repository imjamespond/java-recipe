package com.metasoft.flying.model.action;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.constant.ChessConstant;

/**
 * 行为节点,使用起飞
 * 
 * @author james
 *
 */
public class ItemTakeOffNode extends ActionNode {

	@Override
	public boolean act(Flight flight) {
		int pos = flight.getPos();
		ChessPlayer cp = flight.getChessPlayerByPos(pos);
		if (null == cp) {
			return false;
		}
		
		if((flight.degree & ActionNode.kItem)>0){
		int[] items = cp.getItems();
		if (items[ChessConstant.ITEM_TAKEOFF] > 0) {
			for (Chess chess : cp.getChesses()) {
				if (chess.getState() == 0) {
					FlightHelper.useTakeoff(flight, cp.getUserId(), chess.getChessPos());
					break;
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
