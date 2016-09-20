package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.Chess;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.FlightPve;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.data.PveData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.FlightService;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.NpcService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.vo.PveBeginRequest;
import com.metasoft.flying.vo.PveUpgradeRequest;
import com.metasoft.flying.vo.general.GeneralRequest;

@Controller
public class PveController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private GameRoomService gameRoomService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private NpcService npcService;
	@Autowired
	private FlightService flightService;	
	

	@HandlerAnno(name = "升级", cmd = "pve.upgrade", req = GeneralRequest.class)
	public void upgrade(PveUpgradeRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		self.setUpgrade(req.getUpgrade());
	}
	@HandlerAnno(name = "闯关", cmd = "pve.begin", req = GeneralRequest.class)
	public void begin(PveBeginRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		room.clean();
		Long master = Long.valueOf(room.getName());
		if(master!=self.getId()){
			throw new GeneralException(0, "must.be.in.self.room");
		}
		
		PveData data = staticDataService.pveDataMap.get(req.getLevel());
		if(null==data){
			throw new GeneralException(0, "pve.data.error");
		}
		
		self.getPveNum();
		self.pveEpoch = System.currentTimeMillis();
		self.pveNum++;
		self.updateData = true;
		
		FlightPve flight = room.getPve();
		if(flight==null){
			flight = new FlightPve(room);
			room.setFlightPve(flight);
		}
		flight.reset();
		flight.level = req.getLevel();
		flight.degree = data.getDegree();
		flight.join(room.getMasterId());

		String[] usernames = data.getNembers().split(",");
		int i = 1;
		for(String username:usernames){
			Long uid = staticDataService.pveNpcNameDataMap.get(username);
			flight.join(uid);
			flight.setNpc(i++);
		}
		
		//设置pve棋局
		for(ChessPlayer cp:flight.chessPlayers){
			if(null!=cp&&cp.getUserId()>0){
			int j=0;
			for(Chess chess:cp.getChesses()){
				chess.reset();
				if(++j>data.getPlane()){
					chess.setState(ChessConstant.CHESS_FINISH);
				}
			}
			if(cp.getNpc()==0){
				System.arraycopy(self.getNInitUpgrade(), 0, cp.getItems(), 0,
	                    Math.min(self.getNInitUpgrade().length, cp.getItems().length));
			}
			}
		}
		flight.begin();
		flightService.addFlightDeq(flight);
	}
}
