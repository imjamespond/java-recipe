package com.chitu.chess.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.time.DateUtils;

import com.chitu.chess.msg.RoomUserQuitDto;

import cn.gecko.broadcast.Channel;
import cn.gecko.broadcast.ChannelManager;
import cn.gecko.commons.timer.ScheduleManager;
import cn.gecko.commons.utils.SpringUtils;

public class ChessDistrict implements IChessDistrict {

	public ConcurrentHashMap<Long, ChessRoom> district;
	protected int districtId;
	protected int playerNum;

	private HashSet<Long> availableRooms3;// 已有3个玩家
	private HashSet<Long> availableRooms2;// 2个
	private HashSet<Long> availableRooms1;// 1个
	
	protected ScheduleManager scheduleManager;

	public ChessDistrict(ScheduleManager scheduleManager, int districtId) {
		this.districtId = districtId;
		district = new ConcurrentHashMap<Long, ChessRoom>();
		availableRooms3 = new HashSet<Long>();
		availableRooms2 = new HashSet<Long>();
		availableRooms1 = new HashSet<Long>();

		this.scheduleManager = scheduleManager;
		
		if(districtId == 1001){
			playerNum = 108;
		}
	}
	
	public void start(){
		Runnable chessRoomTimer = new ChessRoomTimer(districtId);
		scheduleManager.scheduleAtFixedRate(chessRoomTimer, DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND * 2);	
	}

	public synchronized ChessRoom getAvailableRoom(long playerId,long preRoomId) {
		ChessUtils.chessLog.info("===========getAvailableRoom===========:"+preRoomId);
		ChessUtils.chessLog.info("=district="+district.toString());
		ChessUtils.chessLog.info("=availableRooms3="+availableRooms3.toString());
		ChessUtils.chessLog.info("=availableRooms2="+availableRooms2.toString());
		ChessUtils.chessLog.info("=availableRooms1="+availableRooms1.toString());
			
		ChessRoom chessRoom = null;
		ChessRoom preChessRoom = null;
		
		//traverse room of 3 player
		Iterator<Long> it = availableRooms3.iterator();
		while ( it.hasNext() ) {
			long chessRoomId = it.next();
			if (!district.containsKey(chessRoomId)) {
				continue;
			}
			chessRoom = district.get(chessRoomId);
			if (chessRoom.id == preRoomId) {//与之前房间相同
				preChessRoom = chessRoom;
				chessRoom = null;
				continue;
			}else{
				break;
			}
		}
		if (chessRoom != null) {
			if (chessRoom.addPlayer(playerId) >= 0) {// 尝试进入, 房间已满
				addAvailRooms(chessRoom);
				ChessUtils.chessLog.info("===========availableRooms3===========");
				return chessRoom;
			}
		}
		
		//traverse room of 2 player 
		it = availableRooms2.iterator();
		while ( it.hasNext() ) {
			long chessRoomId = it.next();
			if (!district.containsKey(chessRoomId)) {
				continue;
			}
			chessRoom = district.get(chessRoomId);
			if (chessRoom.id == preRoomId) {//与之前房间相同
				preChessRoom = chessRoom;
				chessRoom = null;
				continue;
			}else{
				break;
			}
		}			
		if (chessRoom != null) {
			if (chessRoom.addPlayer(playerId) >= 0) {// 尝试进入, 房间已满
				addAvailRooms(chessRoom);
				ChessUtils.chessLog.info("===========availableRooms2===========");
				return chessRoom;
			}
		}
		
		//traverse room of 1 player 
		it = availableRooms1.iterator();
		while ( it.hasNext() ) {
			long chessRoomId = it.next();
			if (!district.containsKey(chessRoomId)) {
				continue;
			}
			chessRoom = district.get(chessRoomId);
			if (chessRoom.id == preRoomId) {//与之前房间相同
				preChessRoom = chessRoom;
				chessRoom = null;
				continue;
			}else{
				break;
			}
		}
		if (chessRoom != null) {
			if (chessRoom.addPlayer(playerId) >= 0) {// 尝试进入, 房间已满
				addAvailRooms(chessRoom);
				ChessUtils.chessLog.info("===========availableRooms1===========");
				return chessRoom;
			}		
		}
		if (preChessRoom != null) {
			if (preChessRoom.addPlayer(playerId) >= 0) {// 尝试进入, 房间已满
				addAvailRooms(preChessRoom);
				ChessUtils.chessLog.info("===========preChessRoom===========");
				return preChessRoom;
			}		
		}

		chessRoom = new ChessRoom(districtId);
		chessRoom.init();
		chessRoom.addPlayer(playerId);
		district.put(chessRoom.id, chessRoom);
		addAvailRooms(chessRoom);
		return chessRoom;
	}


	public synchronized void addAvailableRooms(ChessRoom chessRoom) {
		 addAvailRooms(chessRoom);
	}
	
	private void addAvailRooms(ChessRoom chessRoom) {
		removeAvailRoom(chessRoom.id);
		switch (chessRoom.playerNum) {
		case 4:
			break;
		case 3:
			availableRooms3.add(chessRoom.id);
			break;
		case 2:
			availableRooms2.add(chessRoom.id);
			break;
		case 1:
			availableRooms1.add(chessRoom.id);
			break;
		case 0:
			district.remove(chessRoom.id);
			break;
		}
		ChessUtils.chessLog.info("===========addAvailableRooms===========:" + chessRoom.id + "playerNum:" + chessRoom.playerNum);
	}

	private void removeAvailRoom(long chessRoomId){
		//删除索引
		availableRooms3.remove(chessRoomId);
		availableRooms2.remove(chessRoomId);
		availableRooms1.remove(chessRoomId);
	}

	public synchronized ChessRoom npcNewRoom(long playerId){
		ChessRoom chessRoom = new ChessRoom(districtId);
		chessRoom.init();
		chessRoom.addNpcPlayer(playerId);
		district.put(chessRoom.id, chessRoom);
		addAvailRooms(chessRoom);
		return chessRoom;
	}

	public synchronized int addNpc(ChessRoom chessRoom,long playerId){
		chessRoom.addNpcPlayer(playerId);
		district.put(chessRoom.id, chessRoom);
		addAvailRooms(chessRoom);
		return chessRoom.playerNum;
	}
	
	
	@Override
	public synchronized void leavingCheck(long playerId, IChessRoom chessRoom) {
		if(chessRoom != null){
			chessRoom.setPlayerAbsent(playerId);// 将玩家踢出房间
			ChessUtils.chessLog.info("===========leavingCheck===========:" + "playerNum:" + chessRoom.getPlayerNum());
			removeAvailRoom(chessRoom.getId());
			switch (chessRoom.getPlayerNum()) {
			case 4:
				break;
			case 3:
				availableRooms3.add(chessRoom.getId());
				break;
			case 2:
				availableRooms2.add(chessRoom.getId());
				break;
			case 1:
				availableRooms1.add(chessRoom.getId());
				break;
			case 0:
				district.remove(chessRoom.getId());		
				break;
			}
		
			// 广播给其他人的消息
			ChannelManager channelManager = SpringUtils.getBeanOfType(ChannelManager.class);
			Channel roomChannel = channelManager.getChannel(String.valueOf(chessRoom.getId()));
			RoomUserQuitDto roomUser = new RoomUserQuitDto();
			roomUser.setPlayerId(String.valueOf(playerId));
			roomChannel.broadcast(roomUser);
		}
	}
	
	@Override
	public int getPlayerNum() {
		return playerNum;
	}

	@Override
	public boolean containRoom(long roomId){
		return district.containsKey(roomId);
	}
	
	@Override
	public IChessRoom getIChessRoom(long roomId) {
		return district.get(roomId);
	}
	
	@Override
	public synchronized void incPlayerNum() {
		playerNum++;
	}
	@Override
	public synchronized void decPlayerNum() {
		playerNum = --playerNum < 0 ? 0 : playerNum;
	}
	@Override
	public boolean checkRoomMutex( long playerId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public class ChessRoomTimer implements Runnable {

		public ChessRoomTimer(int districtId) {
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// System.out.println(districtId);

			if (district.size() > 0) {
				// 遍历所有房间
				Iterator<Entry<Long, ChessRoom>> iter = district.entrySet().iterator();
				Map.Entry<Long, ChessRoom> entry;
				while (iter.hasNext()) {
					entry = (Map.Entry<Long, ChessRoom>) iter.next();
					ChessRoom chessRoom = entry.getValue();
					chessRoom.checkState();
				}
			}

		}

	}



}
