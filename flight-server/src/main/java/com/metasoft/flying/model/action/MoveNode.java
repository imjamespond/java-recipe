package com.metasoft.flying.model.action;

import java.util.ArrayDeque;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.vo.ChessFlightVO;
import com.metasoft.flying.vo.ChessItemVO;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * 行为节点,走棋,起飞
 * 
 * @author james
 *
 */
public class MoveNode extends ActionNode {

	@Override
	public boolean act(Flight flight) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return false;
		}
		int[] actions = new int[4];
		int chessnum = 0;
		int chessbits = 0;//可走棋子
		//是否有电磁干扰
		boolean fogCheck = FlightHelper.fogCheck(flight.getChessPlayers(), flight.getCurrentPos());	
		for (Chess chess : cp.getChesses()) {
			int bit = 1<<chessnum;
			//可走棋子
			if (chess.isFlying()) {
				//不受电磁干扰 或 受干扰但不在外面
				if( !fogCheck || (fogCheck && !chess.isOutside())){
					chessbits |= bit;
					actions[chessnum]=chess.getJourney()+10;
				}
			}
			//起飞
			else if (chess.isReady()) {
				chessbits |= bit;
				actions[chessnum]+=5;
			}
			chessnum++;
		}
		
		if((flight.degree & ActionNode.kCheat)>0){
		//检测前方是否有龙卷风或传送门,敌机
		int tornado = -1;
		int teleport = -1;
		for (ChessItemVO item : flight.items) {
			// 龙卷风
			if (item.getItemId() == ChessConstant.INCIDENT_TORNADO) {
				tornado = item.getCoord();
			}else if (item.getItemId() == ChessConstant.INCIDENT_TELEPORT) {
				teleport = item.getCoord();
			}
		}
		chessnum = 0;
		for (Chess chess : cp.getChesses()) {
			int bit = 1<<chessnum;
			if((chessbits&bit)>0){//使用道具后计算不准&&cp.getItemPos()<0
				if(chess.isOutside() && chess.getJourney()>26){//过半程,在外面
					if(FlightHelper.detectTornadoTeleport(chess, flight.getDice(), tornado, teleport, flight)){
						actions[chessnum]=10;
					}
				}
				if(FlightHelper.detectChess(chess, flight)){
					actions[chessnum]+=30;
				}
			}
			chessnum++;
		}
		}
		
		//选择
		int action = -1;
		Chess theChess = null;
		for (int i=0 ;i<cp.getChesses().length; i++) {
			int bit = 1<<i;
			if((chessbits&bit)>0&& action<actions[i]){
				theChess = cp.getChesses()[i];
				action = actions[i];
			}
		}
		
		//走棋
		if(null!=theChess){
			//System.out.println("theChess:"+theChess.getChessPos()+","+chessbits);
			ArrayDeque<Chess> chessGoList = null;
			if(theChess.isFlying()){
				// 找出行程相同的
				chessGoList = flight.getChessByJourney(cp.getChesses(), theChess.getJourney());
			}else if(theChess.isReady()){
				chessGoList = new ArrayDeque<Chess>();
				chessGoList.add(theChess);
			}
			ChessFlightVO vo = new ChessFlightVO();
			vo.setGoList(flight.goCalculate(chessGoList));
			vo.setPos(flight.getPos());	
			// 广播自动走棋计算
			flight.broadcast(GeneralResponse.newObject(vo));
			return true;// 只走一个棋
		}
		
		if (null != next) {
			return next.act(flight);
		}
		return false;
	}
}