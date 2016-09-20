package com.metasoft.flying.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.controller.RankController;
import com.metasoft.flying.controller.RelationController;
import com.metasoft.flying.model.ChessApplication;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.common.ScheduleService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.service.net.GroupService;
import com.metasoft.flying.util.MathUtils;
import com.metasoft.flying.util.RandomUtils;
import com.metasoft.flying.vo.ChessRoomVO;
import com.metasoft.flying.vo.RankRequest;
import com.metasoft.flying.vo.RoomListVO;
import com.metasoft.flying.vo.RoomVO;
import com.metasoft.flying.vo.general.GeneralResponse;

@Service
public class GameRoomService extends GroupService<GameRoom> {
	private static final Logger logger = LoggerFactory.getLogger(GameRoomService.class);

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private UserService userService;
	@Autowired
	private RankService rankService;
	@Autowired
	private RelationController relationController;
	@Autowired
	private RankController rankController;
	@Autowired
	private MatchService matchService;
	
	@Value("${room.list.update}")
	private String listUpdate;
	
	public void init() {
		Runnable runnable = new RoomListTask();
		scheduleService.schedule(runnable, listUpdate);
		runnable.run();//init list
	}

	public GameRoom getGroup(String name) {
		GameRoom room = super.getGroup(name);
		if (room != null)
			return room;
		room = new GameRoom(name);
		long masterId = Long.valueOf(name);
		room.setMasterId(masterId);
		registerGroup(room);
		return room;
	}

	public GameRoom getGroupEntity(String name) {
		return super.getGroup(name);
	}
	
	public boolean isPlaying(User user) {
		if(null!=user.getGroup()){
			GameRoom room = getGroupEntity(user.getGroup());
			if(null!=room){
				Flight flight = room.getFlight();
				if(flight.isFlightBegin()&&flight.getType()!=GeneralConstant.GTYPE_PVE){
					return true;
				}
				//else if(flight.isFlightBegin()&&flight.getType()==GeneralConstant.GTYPE_MATCH){
					//return true;
				//}
				if(matchService.enrollCheck(user.getId())>0){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 女房主在线而且在自己房间，而且使用音频，申请人数最少的优先
	 * 
	 * @return
	 * @throws GeneralException
	 */
	public ChessRoomVO getRandomRoom(long id, int type) throws GeneralException {
		// some timestamp check
		RelationController relationController = SpringService.getBean(RelationController.class);
		int num = 0;
		GameRoom returnRoom = null;
		for (Entry<String, GameRoom> entry : groupMap.entrySet()) {
			GameRoom room = entry.getValue();
			// 无房主
			if (room.getMasterId() == 0l) {
				continue;
			}
			// 不在线
			User user = userService.getOnlineUserById(room.getMasterId());
			if (null == user) {
				continue;
			}
			// 不在房间
			if ((GeneralConstant.ROOM_INSIDE & type) > 0) {
				if (user.getGroup() == null) {
					continue;
				} else if (Long.valueOf(user.getGroup()) != room.getMasterId()) {
					continue;
				}
			}
			// 不是女玩家
			if ((GeneralConstant.ROOM_GIRL & type) > 0) {
				if (GeneralConstant.FEMALE != user.getUserPersist().getGender()) {
					continue;
				}
				
				//if(!user.isMicophone()){
					//continue;
				//}
			}

			// 房主黑名单检测
			if (relationController.inBlackList(user, id)) {
				continue;
			}

			// logger.debug(room.toString() + " group:" + room.groupSize());
			if (returnRoom == null) {
				returnRoom = room;
				num = room.groupSize();
			}
			// 比之前的小
			if (room.groupSize() < num) {
				returnRoom = room;
				num = room.groupSize();
			}
		}
		ChessRoomVO vo = new ChessRoomVO();
		if (null != returnRoom) {
			long masterId = Long.valueOf(returnRoom.getName());
			User master = userService.getAnyUserById(masterId);
			//房主不在线
			if (null == master.getConn()){
				vo.setId(0);
			}	
			else if (null != master.getGroup() ){
				//房主不在自己房间
				if(master.getGroup().indexOf(returnRoom.getName())<0 ){	
					vo.setId(1);
				}else{
					vo.setId(2);
				}
			}else{
				//房主不在自己房间
				vo.setId(1);
			}
			vo.setName(returnRoom.getName());
		} else {
			throw new GeneralException(0, "none.approperiate.room");
		}
		return vo;
	}
	
	public ChessRoomVO getRandomRoom2(long id) throws GeneralException {
		//1， 房主在线，已经有 两个人申请 游戏
		GameRoom apply2 = null;
		//2， 房主在线， 已经有 一个人 申请游戏
		GameRoom apply1 = null;
		//3， 房主在线， 无人申请游戏
		GameRoom apply0 = null;
		//进入自己的 房间， 成为房主
		GameRoom mine = null;
		for (Entry<String, GameRoom> entry : groupMap.entrySet()) {
			GameRoom room = entry.getValue();
			// 无房主
			if (room.getMasterId() == 0l) {
				continue;
			}
			// 不在线
			User user = userService.getOnlineUserById(room.getMasterId());
			if (null == user) {
				continue;
			}
			//开始游戏的
			if ( room.getFlight().isFlightBegin()){
				continue;
			}
			//房主不在房间
			if ( null == user.getGroup()) {
				continue;
			}else if( user.getGroup().indexOf(room.getName())<0 ){
				continue;
			}

			// 房主黑名单检测
			if (relationController.inBlackList(user, id)) {
				continue;
			}
			
			ChessApplication app = room.getChessApp();
			if (app.getSize()==2){
				apply2 = room;
			}else if (app.getSize()==1){
				apply1 = room;
			}else if (app.getSize()==0){
				apply0 = room;
			}
		}
		ChessRoomVO vo = new ChessRoomVO();
		if (null != apply2) {
			//vo.setId(apply2.getMasterId());
			vo.setName(apply2.getName());
		} else if (null != apply1) {
			//vo.setId(apply1.getMasterId());
			vo.setName(apply1.getName());
		} else if (null != apply0) {
			//vo.setId(apply0.getMasterId());
			vo.setName(apply0.getName());
		} else {
			mine = getGroup(String.valueOf(id));
			//vo.setId(mine.getMasterId());
			vo.setName(mine.getName());
		} 
		
		long masterId = Long.valueOf(vo.getName());
		User master = userService.getAnyUserById(masterId);
		//房主不在线
		if (null == master.getConn()){
			vo.setId(0);
		}	
		else if (null != master.getGroup() ){
			//房主不在自己房间
			if(master.getGroup().indexOf(vo.getName())<0 ){	
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
	
	/**随机进入房间
	 * @param id
	 * @return
	 * @throws GeneralException
	 */
	public ChessRoomVO getRandomRoom3(long id) throws GeneralException {
		//1， 房主在线
		//2， 在自己房间
		//3， 申请队列<3
		//4, 游戏没开始
		ChessRoomVO vo = new ChessRoomVO();
		int size = rankService.roomWaiting;
		int random = RandomUtils.nextInt(0,size+1);
		for(int i=0; i<size; i++){
			RoomVO roomVo = rankService.roomList[(i+random)%size];
			if(roomVo==null){
				logger.debug("roomVo==null");
				continue;
			}
			GameRoom room =	groupMap.get(String.valueOf(roomVo.getId()));
			if(room==null){
				logger.debug("room==null");
				continue;
			}
			// 无房主
			if (room.getMasterId() == 0l) {
				logger.debug("room.getMasterId() == 0l");
				continue;
			}
			// 不在线
			User user = userService.getOnlineUserById(room.getMasterId());
			if (null == user) {
				logger.debug("user==null");
				continue;
			}
			//开始游戏的
			if ( room.getFlight().isFlightBegin()){
				logger.debug("getState() > 0");
				continue;
			}
			//房主不在房间
			if ( null == user.getGroup()) {
				logger.debug("getGroup()==null");
				continue;
			}else if( user.getGroup().indexOf(room.getName())<0 ){
				logger.debug("indexOf(room.getName())<0");
				continue;
			}
			//申请数小于3
			ChessApplication app = room.getChessApp();
			if(app.getSize()>3){
				logger.debug("app.getSize()>3");
				continue;
			}
			// 房主黑名单检测
			if (relationController.inBlackList(user, id)) {
				continue;
			}
			vo.setName(room.getName());
		}
		//进入自己房间
		if(vo.getName()==null){
			GameRoom mine = getGroup(String.valueOf(id));
			vo.setName(mine.getName());
		} 
		//获得房间状态
		long masterId = Long.valueOf(vo.getName());
		User master = userService.getAnyUserById(masterId);
		//房主不在线
		if (null == master.getConn()){
			vo.setId(0);
		}	
		else if (null != master.getGroup() ){
			//房主不在自己房间
			if(master.getGroup().indexOf(vo.getName())<0 ){	
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
	
	public long getRoomList() {
		Set<RoomVO> set = new TreeSet<RoomVO>(new Comparator<RoomVO>(){
			@Override
			public int compare(RoomVO o1, RoomVO o2) {

				//o2前,o1后
				if(o1.getState()>0&&o2.getState()==0){
					return 1;
				}
				else if(o1.getState()==0&&o2.getState()>0){
					return -1;
				}
				//
				else if(o1.getNum() < o2.getNum()){
					return 1;
				}
				return -1;
			}
		});

		int count = 9999;
		for(Entry<String, GameRoom> entry:this.groupMap.entrySet()){		
			GameRoom room = entry.getValue();		

			if(room.groupSize()>0){
				long uid = Long.valueOf(room.getName());
				User user = userService.getOnlineUserById(uid);
				if(user==null){//房主不在线
					continue;
				}else if(user.getGroup()!=null&&!user.getGroup().equals(room.getName()) ){//房主不在自己房间
					continue;
				}
				
				RoomVO vo = new RoomVO();
				vo.setId(uid);
				vo.setName(user.getUserPersist().getNickname());
				vo.setNum(room.groupSize());
				Flight f = room.getFlight();
				vo.setState(f.isFlightBegin()?1:0);
				vo.setGender(user.getUserPersist().getGender());
				ChessApplication app = room.getChessApp();
				vo.setApply(app.getSize());
				vo.setOnline(user.getConn()==null?false:true);
				set.add(vo);
				if(--count<0){
					break;
				}
			}
		}
		
		long fingerprint = 0;
		count = 0; 
		Arrays.fill(rankService.roomList, null);
		for(RoomVO vo : set){
			if(count<GeneralConstant.ROOM_LIST_SIZE){
				rankService.roomList[count++] = vo;
				rankService.roomAvailable = count;
				if(vo.getState()==0)
					rankService.roomWaiting = count;
				fingerprint ^= MathUtils.fashHash(vo.getId()^
						MathUtils.fashHash(count)^
						MathUtils.fashHash(vo.getNum()^
						MathUtils.fashHash(vo.getState())));
			}else{
				break;
			}
		}
		return fingerprint;
	}
	
	private class RoomListTask implements Runnable {

		@Override
		public void run() {
			long fingerprint = getRoomList();
			if(fingerprint!=rankService.fingerprint){
				RankRequest req = new RankRequest();
				req.setSize(8);
				req.setOffset(0);
				RoomListVO vo = rankController.roomList(req);
				for(Entry<Long, User> entry:userService.getOnlineUserEntrySet()){
					User user = entry.getValue();
					BaseConnection conn = user.getConn();
					if(conn!=null&&user.getGroup()==null){
						conn.deliver(GeneralResponse.newObject(vo));
					}
				}
			}
			rankService.fingerprint = fingerprint;
			//logger.debug("roomlist update fingerprint:{}", fingerprint);
		}
		
	}
}