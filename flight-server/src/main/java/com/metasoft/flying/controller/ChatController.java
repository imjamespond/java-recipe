package com.metasoft.flying.controller;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessApplication;
import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserFollow;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.BaseGroup;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.RankService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.util.ValidateUtils;
import com.metasoft.flying.vo.ChatConfigRequest;
import com.metasoft.flying.vo.ChatRequest;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.ChessRoomVO;
import com.metasoft.flying.vo.RoomUserVO;
import com.metasoft.flying.vo.TalkVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class ChatController implements GeneralController {
	//private static final Logger logger =  LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private GameRoomService chessRoomService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private ItemController itemController;
	@Autowired
	private ApplyController applyController;
	@Autowired
	private RelationController relationController;
	@Autowired
	private ChessController chessController;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private RankService rankService;

	@HandlerAnno(name = "房间设置", cmd = "chat.level", req = ChatConfigRequest.class)
	public void level(ChatConfigRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		// 广播
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		room.broadcast(GeneralResponse.newObject(VoFactory.getChessRoomVO(room)));
	}

	@HandlerAnno(name = "加入组 List<ChessRoomUserVO>", cmd = "chat.join", req = ChatRequest.class)
	public ChessRoomVO join(ChatRequest req) throws GeneralException {
		if(null==req.getGroup()){
			throw new GeneralException(0, "invalid.uid");
		}
		// 退出之前组
		User self = userService.getRequestUser();
		if (null!=self.getGroup() && !req.getGroup().equals(self.getGroup())) {
			leave(self, self.getGroup());
		}

		String group = req.getGroup();// UUID.randomUUID().toString();
		long masterId = Long.valueOf(group);

		User master = userService.getAnyUserById(masterId);
		BaseConnection masterConn = master.getConn();
		// 房主黑名单检测
		if (relationController.inBlackList(master, self.getId())) {
			throw new GeneralException(0, "in.blacklist");
		}

		// 加入组
		GameRoom room = chessRoomService.getGroup(group);
		if (masterId == self.getId()) {			
			String msg = localService.getLocalString("greet.master", new String[] { master.getUserPersist().getNickname() });
			room.broadcast(GeneralResponse.newObject(new ChatVO(msg)));
		} else {
			String msg = localService.getLocalString("greet.guest", new String[] { master.getUserPersist().getNickname() });
			self.getConn().deliver(GeneralResponse.newObject(new ChatVO(msg)));
			if(masterConn!=null){
				String msg2 = localService.getLocalString("greet.player", new String[] { self.getUserPersist().getNickname() });
				masterConn.deliver(GeneralResponse.newObject(new ChatVO(msg2)));
			}
		}
		room.addUser(self);
		room.broadcast(GeneralResponse.newObject(VoFactory.getRoomUserVO(self, 1)));

		// 关注计数
		UserFollow uf = self.getFollowingMap().get(masterId);
		if (null != uf) {
			uf.addCountByOne();
			self.updateData = true;
		}

		// 推送竞猜数据
//		ChessGuess guess = room.getGuess();
//		if (null != guess) {
//			Integer guessPos = guess.getGuessPos(self.getId());
//			if (null != guessPos) {
//				self.getConn().deliver(GeneralResponse.newObject(new ChessGuessVO(guessPos)));
//			}
//		}

		ChessRoomVO vo = VoFactory.getChessRoomVO(room);
		vo.setNickname(master.getUserPersist().getNickname());
		vo.setAvatar(master.getUserPersist().getAvatar());
		
		//房主不在线
		if (null == masterConn){
			vo.setId(0);
		}	
		else if (null != master.getGroup() ){
			//房主不在自己房间
			if(master.getGroup().indexOf(group)<0 ){	
				vo.setId(1);
			}else{
				vo.setId(2);
			}
		}else{
			//房主不在自己房间
			vo.setId(1);
		}
		return vo;
	}

	@HandlerAnno(name = "离开组", cmd = "chat.leave", req = ChatRequest.class)
	public void leaveRoom(ChatRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		String group = req.getGroup();
		leave(user, group);
	}

	public void leave(User user, String group) throws GeneralException {
		GameRoom room = chessRoomService.getGroup(group);
		Flight flight = room.getFlight();
		//PkArena arena = room.getArena();
		// 离开棋局
		if(flight.isFlightBegin()){
			if (room.getMasterId() == user.getId()) {
				flight.end();
				flight.interrupt();
			}else{
				ChessPlayer cp = flight.getChessPlayer(user.getId());
				if (null != cp) {
					cp.reset2();
					// 当前位置,新的回合
					flight.leave(cp.getPos());
				}
			}
		}
		/*对战结束
		else if(null != arena){
			if (room.getMasterId() == user.getId()) {
				arena.interrupt();
			}else{
				arena.leave(user.getId());
			}
		}*/

		//广播 移除申请
		ChessApplication app = room.getChessApp();
		app.remove(user);
		room.broadcast(GeneralResponse.newObject(applyController.applyList(null)));
		//移除玩家
		room.removeUser(user);
		//广播房主离开
		if (room.getMasterId() == user.getId()) {
			String msg = localService.getLocalString("master.leave",new String[] { user.getUserPersist().getNickname() });
			room.broadcast(GeneralResponse.newObject(new ChatVO(msg)));
		}
		//广播离开
		room.broadcast(GeneralResponse.newObject(VoFactory.getRoomUserVO(user, 0)));
		//
		user.setGroup(null);
	}

	@HandlerAnno(name = "广播组", cmd = "chat.broadcast", req = ChatRequest.class)
	public void broadcast(ChatRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		BaseGroup g = chessRoomService.getGroup(self.getGroup());

		// sensitive word
		ValidateUtils.checkTalkKeyword(req.getMsg());

		ChatVO vo = new ChatVO(req.getMsg());
		vo.setId(self.getId());
		vo.setName(self.getUserPersist().getNickname());
		g.broadcast((GeneralResponse.newObject(vo)));
	}

	@HandlerAnno(name = "私聊", cmd = "chat.talk.to", req = ChatRequest.class)
	public void talkTo(ChatRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		User user = userService.getOnlineUserById(req.getUserId());

		// sensitive word
		ValidateUtils.checkTalkKeyword(req.getMsg());

		BaseConnection bc = user.getConn();
		if (bc != null) {
			TalkVO vo = new TalkVO(req.getMsg());
			vo.setId(self.getId());
			vo.setName(self.getUserPersist().getNickname());
			bc.deliver(GeneralResponse.newObject(vo));
		}
	}
	
	@HandlerAnno(name = "大厅广播", cmd = "chat.hall.broadcast", req = ChatRequest.class)
	public void hallbroadcast(ChatRequest req) throws GeneralException {
		User self = userService.getRequestUser();

		// sensitive word
		ValidateUtils.checkTalkKeyword(req.getMsg());

		ChatVO vo = new ChatVO(req.getMsg());
		vo.setId(self.getId());
		vo.setName(self.getUserPersist().getNickname());
		
		Set<Entry<Long, User>> set = userService.getOnlineUserEntrySet();
		for(Entry<Long, User> entry:set){
			User user = entry.getValue();
			if(user.getGroup()==null){
				BaseConnection bc = user.getConn();
				if (bc != null) {
					bc.deliver(GeneralResponse.newObject(vo));
				}
			}
		}
	}

	@HandlerAnno(name = "成员列表 List<ChessRoomUserVO>", cmd = "chat.memberlist", req = ChatRequest.class)
	public List<RoomUserVO> memberlist(ChatRequest req) {
		String group = req.getGroup();
		BaseGroup g = chessRoomService.getGroup(group);
		return g.getMemberList();
	}

	@HandlerAnno(name = "进入随机房间", cmd = "chat.random.room", req = GeneralRequest.class)
	public ChessRoomVO roomOfRandom(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return chessRoomService.getRandomRoom(self.getId(), GeneralConstant.ROOM_INSIDE);
	}

	@HandlerAnno(name = "进入妹子房间", cmd = "chat.girl.room", req = GeneralRequest.class)
	public ChessRoomVO roomOfGirl(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return chessRoomService.getRandomRoom3(self.getId());
	}
	

}
