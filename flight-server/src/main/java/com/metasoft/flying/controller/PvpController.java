package com.metasoft.flying.controller;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.FlightPvp;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.data.ArenaData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.net.annotation.HandlerType;
import com.metasoft.flying.node.model.Node;
import com.metasoft.flying.node.service.RpcServerService;
import com.metasoft.flying.service.FlightService;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.NpcService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.ArenaRequest;
import com.metasoft.flying.vo.PvpInviteVO;
import com.metasoft.flying.vo.PvpRoomVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class PvpController implements GeneralController {

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
	@Autowired
	private RpcServerService rpcService;
	@Autowired
	private ChatController chatController;
	
	private static final int kUidSize = 4;
	//private long[] uidArr = new long[kUidSize];
	//private HashedWheelTimer hashedTimer = new HashedWheelTimer();

	@HandlerAnno(name = "对战搜寻", cmd = "pvp.search", req = GeneralRequest.class, type = HandlerType.FORWARD)
	public void search2(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		Node node = rpcService.getOnlineNodeById("001");
		if(null != node){
			node.pvpSearch(self.getId(), 0, req.getSerial());
		}
	}
	
	@HandlerAnno(name = "飞行场", cmd = "pvp.airport", req = GeneralRequest.class, type = HandlerType.FORWARD)
	public void search3(ArenaRequest req) throws GeneralException {
		User self = userService.getRequestUser();			
		ArenaData ad = staticDataService.arenaMap.get(req.getId());
		if(null==ad){
			throw new GeneralException(0, "invalid.arena");
		}
		self.reduceGold(ad.getCost());
		// 财富更新
		RequestUtils.getCurrentConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));

		
		Node node = rpcService.getOnlineNodeById("001");
		if(null != node){
			node.pvpSearch(self.getId(), ad.getId(), req.getSerial());
		}
	}
	
	public void begin2(List<Long> list, int id) throws GeneralException{
		StaticDataService staticDataService = SpringService.getBean(StaticDataService.class);
		ArenaData ad = staticDataService.arenaMap.get(id);
		int cost=0,gold = 0;
		if(null!=ad){
			cost = ad.getCost();
			gold = ad.getGold();
		}
		if(list.size()==0){
			return;
		}
		long uid = list.get(0);
		GameRoom room = gameRoomService.getGroup(String.valueOf(uid));
		User master = userService.getAnyUserById(uid);
		room.clean();//清理房间
		FlightPvp flight = new FlightPvp(room);
		flight.reset();//重置
		flight.degree = 5;//难度
		flight.gold = gold;
		if(cost>0)
		flight.setType(GeneralConstant.GTYPE_PVP);
		room.setFlightPvp(flight);
		//清理,进房间
		for(int i=0; i<kUidSize; i++){
			if(i<list.size()&&list.get(i)>0){
				join(room,list.get(i));
			}
		}
		for(int i=0; i<kUidSize; i++){
			if(i<list.size()&&list.get(i)>0){
				flight.join(list.get(i),i);
			}else{
				Long npc = npcService.getNextPlayer();
				flight.join(npc,i);
				flight.setNpc(i);	
			}
		}
		//广播pvp房间资讯
		PvpRoomVO vo = VoFactory.getPvpRoomVO(room);
		vo.setNickname(master.getUserPersist().getNickname());
		vo.setAvatar(master.getUserPersist().getAvatar());
		room.broadcast(GeneralResponse.newObject(vo));

		flight.begin();
		flightService.addFlightDeq(flight);
	}
	
	public void sendInvitation(long uid){
		User user=null;
		try {
			user = userService.getAnyUserById(uid);
		} catch (GeneralException e) {
			e.printStackTrace();
			return;
		}
		PvpInviteVO vo = new PvpInviteVO(uid, user.getUserPersist().getNickname());
		//邀请广播
		for(Entry<Long, User> entry:userService.getOnlineUserEntrySet()){
			User u = entry.getValue();
			BaseConnection conn = u.getConn();
			if(null!=conn&&
					u.getId()!=uid&&//自己
					u.getUserPersist().getGender()>0&&//新手
					!gameRoomService.isPlaying(u)){	
				conn.deliver(GeneralResponse.newObject(vo));
			}
		}
	} 
	
	public void join(GameRoom room, long uid) throws GeneralException {
		User user = userService.getAnyUserById(uid);
		
		String group = user.getGroup();
		if (null!=group ){
			GameRoom currentRoom = gameRoomService.getGroup(group);
			if(!room.getName().equals(group)) {//不在房主房间
				//移除玩家
				currentRoom.removeUser(user);
			}
			// 离开棋局
			currentRoom.clean();
		}

		// 加入组
		room.addUser(user);
	}

}
