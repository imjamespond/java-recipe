package com.metasoft.flying.model.action;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.constant.ChessConstant;

/**
 * 行为节点,使用其它道具
 * 
 * @author james
 *
 */
public class ItemNode extends ActionNode {

	@Override
	public boolean act(Flight flight) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return false;
		}
		
		if((flight.degree & ActionNode.kItem)>0){
		boolean useitem = false;
		boolean enemy = false;
		for (Chess chess : cp.getChesses()) {
			if ((ChessConstant.CHESS_FLIGHT & chess.getState()) > 0 
					&& chess.getJourney() > 0
					&& chess.getJourney() < ChessConstant.JOURNEY_OUT) {
				useitem = true;
				if(FlightHelper.detectEnemy(chess, 6, flight)){
					enemy = true;
				}
			}
		}
		if(useitem){
		int[] items = cp.getItems();//System.out.println(Arrays.toString(items));
		if (items[ChessConstant.ITEM_THORNS] > 0) {
			if(!cp.isFog(flight.getCurrentPos()) && !cp.isThorns(flight.getCurrentPos()) && enemy){
				FlightHelper.useThorns(flight, cp.getUserId());
			}
		}else if(items[ChessConstant.ITEM_FOG] > 0){
			if(!cp.isFog(flight.getCurrentPos()) && !cp.isThorns(flight.getCurrentPos()) && enemy){
				FlightHelper.useFog(flight, cp.getUserId());
			}
		}
		if(items[ChessConstant.ITEM_REFUEL] > 0){
			if(cp.getRefuel() == 0){
				FlightHelper.useRefuel(flight);
			}
		}else if(items[ChessConstant.ITEM_ENHANCE] > 0){
			cp.setItemPos(ChessConstant.ITEM_ENHANCE);
			flight.diceTrick( ChessConstant.ITEM_ENHANCE);
		}else if(items[ChessConstant.ITEM_RELAY] > 0){
			cp.setItemPos(ChessConstant.ITEM_RELAY);
			flight.diceTrick( ChessConstant.ITEM_RELAY);
		}else if(items[ChessConstant.ITEM_MAGIC] > 0){
			cp.setItemPos(ChessConstant.ITEM_MAGIC);
			cp.reduce(ChessConstant.ITEM_MAGIC);
			if(!flight.isFlightDiced()){
				flight.diceCaculate(6);
			}
			return true;
		}			
		}
		}

		if (null != next) {
			return next.act(flight);
		}
		return false;
	}
}
