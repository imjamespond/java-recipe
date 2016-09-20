package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessApplication;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightHelper;
import com.metasoft.flying.model.FlightNpc;
import com.metasoft.flying.model.FlightRp;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.RosePresentData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.FlightService;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.NpcService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.ChessAnimRequest;
import com.metasoft.flying.vo.ChessBeginRequest;
import com.metasoft.flying.vo.ChessDiceRequest;
import com.metasoft.flying.vo.ChessFinishVO;
import com.metasoft.flying.vo.ChessGoRequest;
import com.metasoft.flying.vo.ChessInfoVO;
import com.metasoft.flying.vo.ChessReadyRequest;
import com.metasoft.flying.vo.ChessRequest;
import com.metasoft.flying.vo.ChessRoomUserVO;
import com.metasoft.flying.vo.ItemRequest;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class ChessController implements GeneralController {
	//private static final Logger logger =  LoggerFactory.getLogger(ChessController.class);
	@Autowired
	private NpcService npcService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameRoomService gameRoomService;
	@Autowired
	private FlightService flightService;	
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private LocalizationService localizationService;
	@Autowired
	private ChatController chatController;
	@Autowired
	private ItemController itemController;
	@Autowired
	private ApplyController applyController;

	@HandlerAnno(name = "房主指定玩家", cmd = "chess.designate", req = ChessRequest.class)
	public void designate(ChessRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		flight.join(req.getUserId(), req.getPos());

		// 广播加入
		User user = userService.getOnlineUserById(req.getUserId());
		if (user == null) {
			user = userService.getAnyUserById(req.getUserId());
			flight.setAutoState(user.getId());
		}

		ChessRoomUserVO vo = new ChessRoomUserVO();
		vo.setPos(req.getPos());
		vo.setUserId(req.getUserId());
		vo.setName(user.getUserPersist().getNickname());
		room.broadcast(GeneralResponse.newObject(vo));
	}

	@HandlerAnno(name = "房主开局", cmd = "chess.begin", req = ChessRequest.class)
	public void begin(ChessBeginRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		room.clean();
		Flight flight = room.getFlight();

		if (room.getMasterId() != self.getId()) {
			throw new GeneralException(0, "must.be.room.host");
		}

		try {
			//加入玩家
			for(int i=0; i<req.getUserIdList().length&&i<flight.getChessPlayers().length; i++){
				if(room.containMember(req.getUserIdList()[i])&&i<flight.getChessPlayers().length){
					flight.join(req.getUserIdList()[i], i);
				}
			}
		} catch (Exception e) {
			flight.reset();
			throw e;
		}
		
		// 设置魔力色子限制
		if (req.getDice() > 0 && req.getDice() < 5) {
			flight.setMagicDiceNum(req.getDice());
		} else {
			flight.setMagicDiceNum(ChessConstant.MAGIC_DICE_NUM);
		}

		flight.begin();
		flightService.addFlightDeq(flight);

		// 删除申请
		ChessApplication app = room.getChessApp();
		ChessPlayer[] cps = flight.getChessPlayers();
		for (int i = 0; i < cps.length; i++) {
			ChessPlayer cp = cps[i];
			if (null != cp) {
				if (cp.getUserId() > 0) {
					app.remove(cp.getUserId());
				}
			}
		}

		// 广播更新申请列表
		room.broadcast(GeneralResponse.newObject(applyController.applyList(null)));
	}

	@HandlerAnno(name = "赠送规定玫瑰", cmd = "chess.give", req = GeneralRequest.class)
	public Integer give(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		ChessPlayer cp = flight.getChessPlayer(self.getId());
		if (null != cp) {
			ItemRequest itemReq = new ItemRequest();
			itemReq.setItemId(ItemConstant.ITEMID_ROSE1);
			itemReq.setNum(1);
			itemReq.setUserId(Long.valueOf(self.getGroup()));
			itemController.give(itemReq);
			cp.setRose(cp.getRose() + 1);
			giveTo(self, cp);
			return cp.getRose();
		}
		return 0;
	}

	@HandlerAnno(name = "购买赠送规定玫瑰", cmd = "chess.buy.give", req = GeneralRequest.class)
	public Integer buyNGive(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		ChessPlayer cp = flight.getChessPlayer(self.getId());
		if (null != cp) {
			ItemRequest itemReq = new ItemRequest();
			itemReq.setItemId(ItemConstant.ITEMID_ROSE1);
			itemReq.setNum(1);
			itemReq.setUserId(Long.valueOf(self.getGroup()));
			itemController.buyNGive(itemReq);
			cp.setRose(cp.getRose() + 1);
			giveTo(self, cp);
			return cp.getRose();
		}
		return 0;
	}

	private void giveTo(User self, ChessPlayer cp) throws GeneralException {
		// 规定效果
		RosePresentData rp = staticDataService.getRosePresentData(cp.getRose());
		if (null != rp) {
			if (rp.getEffect() == RosePresentData.EFFECT1) {
				self.addItem(ItemConstant.ITEMID_MAGIC_DICE, rp.getNum(), "规定赠送");
				// 物品通知
				self.getConn().deliver(
						GeneralResponse.newObject(VoFactory.getItemVO(ItemConstant.ITEMID_MAGIC_DICE, self)));
			} else if (rp.getEffect() == RosePresentData.EFFECT2) {
				self.addScore(rp.getNum());
				// 财富通知
				self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
			} else if (rp.getEffect() == RosePresentData.EFFECT3) {
				self.addContribution(rp.getNum());
				// 财富通知
				self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
			}
		}
	}

	@HandlerAnno(name = "扔色子", cmd = "chess.dice", req = ChessRequest.class)
	public Integer dice(ChessDiceRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		// 魔力色子检测
/*		ChessPlayer cp = room.getChessPlayer(self.getId());
		if (null == cp) {
			throw new GeneralException(0, "invalid.player");
		}
		if (req.getDice() > 0) {
			if (cp.getMagic() > room.getMagicDiceNum()) {
				throw new GeneralException(0, "magic.dice.limit");
			}
			try {
				UserItem item = self.addItem(ItemConstant.ITEMID_MAGIC_DICE, -1, "扔");
				// 物品通知
				self.getConn().deliver(
						GeneralResponse.newObject(VoFactory.getItemVO(item)));
			} catch (Exception e) {
				throw new GeneralException(0, "insufficient.magic.dice");
			}
			cp.setMagic(cp.getMagic() + 1);
		}*/

		flight.dice(req.getDice(),req.getItem());
		return 0;
	}

	@HandlerAnno(name = "购买扔色子", cmd = "chess.buy.dice", req = ChessRequest.class)
	public Integer buyNDice(ChessDiceRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		ItemRequest itemReq = new ItemRequest();
		itemReq.setItemId(ItemConstant.ITEMID_MAGIC_DICE);
		itemReq.setNum(1);
		itemReq.setUserId(self.getId());
		itemController.buy(itemReq);
		return dice(req);
	}

	@HandlerAnno(name = "棋子准备起飞", cmd = "chess.ready", req = ChessReadyRequest.class)
	public void ready(ChessReadyRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		flight.ready(user.getId(), req.getPos());
	}

	@HandlerAnno(name = "走棋", cmd = "chess.go", req = ChessGoRequest.class)
	public void go(ChessGoRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		flight.go(user.getId(), req.getBits());
	}

	@HandlerAnno(name = "棋局动画播完", cmd = "chess.animate", req = ChessRequest.class)
	public void animateDone(ChessAnimRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		flight.animateDone(user.getId(),req.getTurn(),req.getCount());
	}

	@HandlerAnno(name = "棋局结束", cmd = "chess.end", req = ChessRequest.class)
	public void end(ChessRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		flight.end();
		// 广播结束
		ChessFinishVO vo = new ChessFinishVO();
		vo.setPos(-1);
		room.broadcast(GeneralResponse.newObject(vo));
	}

	@HandlerAnno(name = "棋局信息", cmd = "chess.info", req = ChessRequest.class)
	public ChessInfoVO info(ChessRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		//取消自动
		//ChessPlayer cp = flight.getChessPlayer(user.getId());
		//if(null != cp){
			//cp.setAutoDisable();
		//}
		
		ChessInfoVO vo = VoFactory.getChessInfoVO(flight);
		return vo;
	}

	@HandlerAnno(name = "自动操作", cmd = "chess.setauto", req = ChessRequest.class)
	public void setAuto(ChessRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		flight.setAutoState(user.getId());
	}

	@HandlerAnno(name = "结束自动操作", cmd = "chess.quitauto", req = ChessRequest.class)
	public void quitAuto(ChessRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		Flight flight = room.getFlight();
		flight.quitAutoState(user.getId());
	}
	
	@HandlerAnno(name = "起飞,返回道具数量,-1表示出错", cmd = "chess.use.takeoff", req = ChessRequest.class)
	public Integer useTakeoff(ChessReadyRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		flight.resetDeadline();

		return FlightHelper.useTakeoff(flight, self.getId(), req.getPos());
	}
	
	@HandlerAnno(name = "驱散风卷风,返回道具数量,-1表示出错", cmd = "chess.use.dispel", req = ChessRequest.class)
	public Integer useDispel(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();

		return FlightHelper.useDispel(flight);
	}
	
	@HandlerAnno(name = "荆棘装甲,返回道具数量,-1表示出错", cmd = "chess.use.thorns", req = ChessRequest.class)
	public Integer useThorns(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		flight.resetDeadline();

		return FlightHelper.useThorns(flight, self.getId());
	}
	
	@HandlerAnno(name = "迷雾导弹,返回道具数量,-1表示出错", cmd = "chess.use.fog", req = ChessRequest.class)
	public Integer useFog(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		flight.resetDeadline();

		return FlightHelper.useFog(flight, self.getId());
	}
	
	@HandlerAnno(name = "空中加油,返回道具数量,-1表示出错", cmd = "chess.use.refuel", req = ChessRequest.class)
	public Integer useRefuel(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		flight.resetDeadline();

		return FlightHelper.useRefuel(flight);
	}
	
	@HandlerAnno(name = "扔色子重新计时", cmd = "chess.reset.timer", req = ChessRequest.class)
	public void resetTimer(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Flight flight = room.getFlight();
		flight.resetDeadline();
	}
	
	@HandlerAnno(name = "新手开始", cmd = "chess.npc.begin", req = ChessRequest.class)
	public void npcBegin(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Long master = Long.valueOf(room.getName());
		if(master!=self.getId()){
			throw new GeneralException(0, "must.be.in.self.room");
		}
		FlightNpc flight = room.getNpc();
		if(flight==null){
			flight = new FlightNpc(room);
			room.setFlightNpc(flight);
		}
		
		Long npc = npcService.npc.getId();
		flight.reset();
		flight.join(room.getMasterId());
		flight.join(npc);
		flight.setNpc(1);	
		flight.begin();
		flightService.addFlightDeq(flight);
		
		BaseConnection conn = RequestUtils.getCurrentConn();
		//广播一些
		ChatVO chat = new ChatVO(localizationService.getLocalString("system.first" ));	
		chat.setId(-3);
		chat.setName("");
		conn.deliver(GeneralResponse.newObject(chat));
		chat.setId(-2);
		chat.setMsg(localizationService.getLocalString("system.second" ));
		conn.deliver(GeneralResponse.newObject(chat));
	}	
	@HandlerAnno(name = "rp开始", cmd = "chess.rp.begin", req = ChessRequest.class)
	public void rpBegin(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		
		GameRoom room = gameRoomService.getGroup(self.getGroup());
		Long master = Long.valueOf(room.getName());
		if(master!=self.getId()){
			throw new GeneralException(0, "must.be.in.self.room");
		}
		FlightRp flight = room.getRp();
		if(flight==null){
			flight = new FlightRp(room);
			room.setFlightRp(flight);
		}
		
		Long npc = npcService.getOnePlayer();
		flight.reset();
		flight.join(room.getMasterId());
		flight.join(npc);	
		flight.setNpc(1);	
		flight.begin();
		flightService.addFlightDeq(flight);
	}
}
