package com.metasoft.flying.controller;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.node.model.Node;
import com.metasoft.flying.node.service.RpcServerService;
import com.metasoft.flying.service.FlightService;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.NpcService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.util.ValidateUtils;
import com.metasoft.flying.vo.ChatRequest;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.GmChessItemRequest;
import com.metasoft.flying.vo.GmRequest;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;
import com.qianxun.model.BaseExchangeProtos.BaseExchange;
import com.qianxun.model.BaseExchangeProtos.GemExchange;
import com.qianxun.service.ExchangeToWebService;

@Controller
public class GmController implements GeneralController {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserPersistService userPersistService;
	@Autowired
	private GameRoomService chatService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private StaticDataController staticDataCtr;
	@Autowired
	private ExchangeToWebService exchangeToWebService;
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private GameRoomService gameRoomService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private NpcService npcService;
	@Autowired
	private RpcServerService rpcService;
	

	@HandlerAnno(name = "GM指令 gmCmd:gems钻石,rose玫瑰,con贡献", cmd = "gm.cmd", req = GmRequest.class)
	public void gm(GmRequest req) throws GeneralException {
		logger.info("gm:{}, num:{}",req.getGmCmd(),req.getNum());

		if (System.getProperty("gm") != null) {
			User user = userService.getAnyUserById(req.getUserId());
			if (req.getGmCmd().indexOf("gems") >= 0) {
				user.addGems(req.getNum(), "GM");
				if (null != user.getConn()) {
					user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
				}

			}

			if (req.getGmCmd().indexOf("rose") >= 0) {
				user.addRose(req.getNum(), "GM");
				if (null != user.getConn()) {
					user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
				}
			}

			if (req.getGmCmd().indexOf("apple") >= 0) {
				user.addItem(ItemConstant.ITEMID_APPLE1, req.getNum(), "GM");
				if (null != user.getConn()) {
					user.getConn().deliver(
							GeneralResponse.newObject(VoFactory.getItemVO(ItemConstant.ITEMID_APPLE1, user)));
				}
			}
		}
		
		if (req.getGmCmd().indexOf("npc") >= 0) {
			User self = userService.getRequestUser();
			if (null == self.getGroup()) {
				throw new GeneralException(0, "must.be.in.a.room");
			}
			
			GameRoom room = gameRoomService.getGroup(self.getGroup());
			//FlightNpc npc = new FlightNpc(room);
			Flight flight = room.getFlight();
			Long[] players = npcService.getPlayers();
			
			if (null == flight.chessPlayers[0]) {
				flight.chessPlayers[0] = new ChessPlayer(0);
			}
			flight.chessPlayers[0].setUserId(room.getMasterId());
			flight.chessPlayers[0].setPos(0);

			//加入玩家
			int i = 0;
			for(int j=1; j<flight.chessPlayers.length; j++){
				if (null == flight.chessPlayers[j]) {
					flight.chessPlayers[j] = new ChessPlayer(j);
				}

				if(i<players.length){
					flight.chessPlayers[j].setUserId(players[i]);
					flight.chessPlayers[j].setPos(j);
					flight.chessPlayers[j].setAutoState(ChessConstant.AUTO_ENABLE);
				}
				i++;
			}
			
			flight.begin();
			flightService.addFlightDeq(flight);
		}
		
		if (req.getGmCmd().indexOf("con") >= 0) {

			if (req.getNum() == 110) {
				BaseExchange.Builder bbuilder = BaseExchange.newBuilder();
				GemExchange.Builder gbuilder = GemExchange.newBuilder();
				gbuilder.setDiscription("test exchange");
				gbuilder.setCount(0);
				gbuilder.setTrend("");
				gbuilder.setUid(0l);
				bbuilder.setType(ExchangeToWebService.CMD_GEM_EXCHANGE);
				bbuilder.setExtension(GemExchange.gemExchange, gbuilder.build());
				exchangeToWebService.put(bbuilder.build());
				return;
			}

			// 测试red5
			Node node = rpcService.getOnlineNodeById("media-1");
			if (null != node) {
				// node.forceClose();
				node.cleanStream(String.valueOf(req.getNum()));
			}
		}

	}

	@HandlerAnno(name = "服务器信息", cmd = "gm.server.info", req = GeneralRequest.class)
	public ChatVO serverInfo(GeneralRequest req) throws GeneralException {
		StringBuilder sb = new StringBuilder();
		sb.append("连接数:").append(connectionService.getConnectionNumber());
		sb.append(", 用户数:").append(userService.getOnlineCount());
		sb.append(", 离线数:").append(userService.getOfflineCount());
		sb.append(", 房间数:").append(gameRoomService.getGroupSize());
		sb.append(", 棋局数:").append(flightService.getDequeSize());
		sb.append(", 比赛数:").append(matchService.getMatchSize());
		sb.append(",用户列表:");
		for(Entry<Long, User> entry:userService.getOnlineUserEntrySet()){
			User user = entry.getValue();
			UserPersist up = user.getUserPersist();
			sb.append(" ").append(up.getId()).append("-").append(up.getNickname());
		}
		sb.append(", 房间列表:\n");
		for(Entry<String, GameRoom> entry : gameRoomService.getEntrySet()){
			GameRoom room = entry.getValue();
			sb.append(" ").append(room.getName()).append(":");
			Flight flight = room.getFlight();
			if(flight.isFlightBegin()){
				sb.append("游戏中:");
				
				for(ChessPlayer cp:flight.chessPlayers){
					if(null!=cp&&cp.getUserId()>0){
						long uid = cp.getUserId();
						User user = userService.getAnyUserById(uid);
						sb.append(user.getUserPersist().getNickname()).append(",");
					}
				}
			}
			sb.append("房间中:");
			for(Entry<Long, Boolean> uidEntry : room.getEntrySet()){
				long uid = uidEntry.getKey();
				User user = userService.getAnyUserById(uid);
				sb.append(user.getUserPersist().getNickname()).append(",");
			}
			sb.append("\n");
		}
		ChatVO vo = new ChatVO(sb.toString());

		return vo;
	}

	@HandlerAnno(name = "GM重新载入配置", cmd = "gm.config.override", req = GeneralRequest.class)
	public void serverOverride(GeneralRequest req) throws GeneralException {
		staticDataService.init();
		ValidateUtils.reset();
	}

	@HandlerAnno(name = "全局广播", cmd = "gm.broadcast", req = ChatRequest.class)
	public void broadcast(ChatRequest req) throws GeneralException {
		ChatVO vo = new ChatVO(req.getMsg());
		vo.setId(-1l);
		vo.setName("");
		connectionService.broadcast(GeneralResponse.newObject(vo));
	}
	
	
	
	@HandlerAnno(name = "指定道具位置", cmd = "gm.chess.item", req = GmChessItemRequest.class)
	public void go(GmChessItemRequest req) throws GeneralException {
		if (System.getProperty("gm") == null) {
			return;
		}
		
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		for(int i = 0; i<flight.items.length; i++){
			//FIXME some tricks!
			if(flight.items[i].getItemId() == req.getItemId()){
				flight.items[i].setCoord(req.getCoord());
			}
		}
		room.broadcast(GeneralResponse.newObject(VoFactory.getChessItemListVO(flight)));
		flight.randomItem = 9999;// no longer update
	}
	/*
	@HandlerAnno(name = "指定龙卷风吹落位置", cmd = "gm.chess.tornado", req = GmChessItemRequest.class)
	public void tornado(GmChessItemRequest req) throws GeneralException {
		if (System.getProperty("gm") == null) {
			return;
		}
		
		ChessConstant.TORNADO_FALL = req.getCoord();
	}*/
}
