package com.chitu.chess.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.timer.ScheduleManager;
import com.chitu.chess.data.StaticDistrictTypes;
import com.chitu.chess.data.StaticMatch;
import com.chitu.chess.model.ChessDistrict;
import com.chitu.chess.model.ChessDistrictMatch;
import com.chitu.chess.model.ChessErrorCodes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.model.ChessPlayerEx;
import com.chitu.chess.model.ChessRoom;
import com.chitu.chess.model.ChessRoomMatch;
import com.chitu.chess.model.ChessUtils;
import com.chitu.chess.model.IChessDistrict;
import com.chitu.chess.model.IChessRoom;
import com.chitu.chess.msg.ChessPlayerMatchRankListDto;
import com.chitu.chess.msg.RoomUserMatchPrizeDto;

/**
 * @author Administrator
 *
 */
@Service
public class ChessRoomManager {

	public Map<Integer, IChessDistrict> districts = new ConcurrentHashMap<Integer, IChessDistrict>();
	public Map<Long, ChessPlayerEx> playerRoom = new ConcurrentHashMap<Long, ChessPlayerEx>();// 玩家房间记录
	public Map<Long, Long> npcRoom = new ConcurrentHashMap<Long, Long>();// 玩家房间记录

	@Autowired
	private ScheduleManager scheduleManager;

	public ChessRoomManager() {

	}

	@PostConstruct
	public void init() {

	}
	
	
	/**
	 * 
	 * 共用
	 * @param districtId
	 * @return
	 */
	public void setDistrict(long playerId,int districtId) {
		if(playerRoom.containsKey(playerId)){
			ChessPlayerEx p = playerRoom.get(playerId);
			p.districtId = districtId;
		}else{
			ChessPlayerEx p = new ChessPlayerEx();
			p.districtId = districtId;
			playerRoom.put(playerId, p);
		}
	}
	
	public int getDistrict(long playerId) {
		if(playerRoom.containsKey(playerId)){
			ChessPlayerEx p = playerRoom.get(playerId);
			return p.districtId;
		}
		return 0;
	}

	
	public long getRoomId(long playerId) {
		ChessUtils.chessLog.info("==========playerId============:" + playerId);
		if (playerRoom.containsKey(playerId)) {
			return playerRoom.get(playerId).chessRoomId;
		} else {
			return 0;
		}
	}

	public void setRoomId(long playerId, long roomId) {
		if (playerRoom.containsKey(playerId)) {
			playerRoom.get(playerId).chessRoomId = roomId;
		}
	}

	
	/**
	 * 
	 * 接口调用
	 * @param districtId
	 * @return
	 */
	public boolean containsDistrictId(int districtId) {
		return districts.containsKey(districtId);
	}
	public int getDistPlayerNum(int districtId) {
		if (containsDistrictId(districtId)) {
			return  districts.get(districtId).getPlayerNum();
		} else {
			return 0;
		}
	}

	public void decDistPlayerNum(int districtId) {
		if (containsDistrictId(districtId)) {
			districts.get(districtId).decPlayerNum();
			ChessUtils.chessLog.info("\n\n\nincDistPlayerNum:"+getDistPlayerNum(districtId));
		}
	}

	public void incDistPlayerNum(int districtId) {
		if (containsDistrictId(districtId)) {
			districts.get(districtId).incPlayerNum();
			ChessUtils.chessLog.info("\n\n\nincDistPlayerNum:"+getDistPlayerNum(districtId));
		}
	}

	public boolean checkRoomMutex(long playerId) {
		int districtId = getDistrict(playerId);
		if (containsDistrictId(districtId)) {
			return districts.get(districtId).checkRoomMutex(playerId);
			//ChessUtils.chessLog.info("\n\n\nincDistPlayerNum:"+getDistPlayerNum(districtId));
		}
		return false;
	}
	
	public IChessRoom getIChessRoom(ChessPlayer player) {
		
		
		ChessPlayerEx p;
		if(playerRoom.containsKey(player.id)){
			p = playerRoom.get(player.id);
		}else{
			ChessUtils.chessLog.info("getIChessRoom玩家没进入此区\n\n\n");
			return null;
		}
		
		// 是否存在 此区
		IChessDistrict d;
		if (districts.containsKey(p.districtId)) {
			d = districts.get(p.districtId);
		} else {
			ChessUtils.chessLog.info("不存在 此区\n\n\n");
			return null;
		}
		// 此区是否存在 该房间
		long roomId = getRoomId(player.id);
		if (roomId > 0) {
			return d.getIChessRoom(roomId);
		} else {
			ChessUtils.chessLog.info("此区不存在 该房间\n\n\n");
			return null;
		}
	}

	public void leaveRoom(ChessPlayer player) {
		
		ChessPlayerEx p;
		if(playerRoom.containsKey(player.id)){
			p = playerRoom.get(player.id);
		}else{
			ChessUtils.chessLog.info("leaveRoom暂无玩家信息\n\n\n");
			return;
		}
		
		// 不存在该区
		if (!districts.containsKey(p.districtId)) {
			ChessUtils.chessLog.info("leaveRoom玩家没进入此区\n\n\n");
			return;
		}

		IChessDistrict d = districts.get(p.districtId);
		IChessRoom chessRoom = null;//// 不存在该房间
		long roomId = getRoomId(player.id);
		if (d.containRoom(roomId)) {
			// 该区减人
			decDistPlayerNum(p.districtId);
			p.districtId = 0;
			chessRoom = d.getIChessRoom(roomId);//
		}
		player.leaveChannel(String.valueOf(p.districtId));// 玩家离开频道
		player.leaveChannel(String.valueOf(roomId));// 玩家离开房间

		d.leavingCheck(player.id, chessRoom);
		ChessUtils.chessLog.info("leaveChannel:" + String.valueOf(roomId));
		ChessUtils.chessLog.info("(player.id):" + String.valueOf(player.id));
		ChessUtils.chessLog.info("(player.nickname):" + String.valueOf(player.nickname));
	}
	
	
	

	/**
	 * 动态建区 普通区
	 * @param districtId
	 * @return
	 */
	public synchronized int constructDistrictId(int districtId) {
		if (containsDistrictId(districtId)) {
			return districtId;
		} else {
			StaticDistrictTypes staticDistrictType = StaticDistrictTypes.get(districtId);
			if (null == staticDistrictType) {
				return 0;
			} else {
				ChessDistrict d = new ChessDistrict(scheduleManager, districtId);
				d.start();
				districts.put(districtId, d);
				return districtId;
			}
		}
	}

	public ChessRoom getAvailableRoom(ChessPlayer player, int districtId) {

		ChessRoom chessRoom = null;
		// 不存在该区
		if (!districts.containsKey(districtId)) {
			return null;
		}

		// 该区加人
		incDistPlayerNum(districtId);

		ChessDistrict d = (ChessDistrict) districts.get(districtId);
		// 查找之前的房间
		long roomId = getRoomId(player.id);
		// 查找之前的房间
		if (d.district.containsKey(roomId)) {
			chessRoom = d.district.get(roomId);
			if (chessRoom.setPlayer(player.id) >= 0) {
				return chessRoom;
			}
		}

		// 查找/创建新房间
		chessRoom = d.getAvailableRoom(player.id, roomId);

		setRoomId(player.id, chessRoom.id);// 玩家与房间关联
		ChessUtils.chessLog.info("getAvailableRoom:" + chessRoom.getId());
		return chessRoom;
	}

	public void addAvailableRoom(ChessRoom chessRoom) {
		// 不存在该区
		if (!districts.containsKey(chessRoom.districtId)) {
			return;
		}
		ChessDistrict d = (ChessDistrict) districts.get(chessRoom.districtId);
		d.addAvailableRooms(chessRoom);
	}





	public ChessRoom npcNewRoom(int distId, long playerId) {
		// 不存在该区
		if (constructDistrictId(distId) == 0) {
			return null;
		}
		ChessDistrict d;
		// npc是否已加入
		if (npcRoom.containsKey(playerId)) {
			ChessUtils.chessLog.info(" npc已加入:" + String.valueOf(playerId));
			return null;
		}
		// 是否存在 此区
		if (districts.containsKey(distId)) {
			d = (ChessDistrict) districts.get(distId);
			ChessRoom chessRoom = d.npcNewRoom(playerId);
			npcRoom.put(playerId, chessRoom.id);
			return chessRoom;
		}

		return null;
	}

	public void npcAdd(ChessRoom chessRoom, int distId, long playerId) {
		// 不存在该区
		if (constructDistrictId(distId) == 0) {
			throw new GeneralException(ChessErrorCodes.CHANNEL_NOT_EXIST);
		}
		ChessDistrict d;
		// npc是否已加入
		if (npcRoom.containsKey(playerId)) {
			return;
		}
		// 是否存在 此区
		if (districts.containsKey(distId)) {
			d = (ChessDistrict) districts.get(distId);
			d.addNpc(chessRoom, playerId);
			npcRoom.put(playerId, chessRoom.id);
		}
	}

	public int npcClean(int distId) {
		// 不存在该区
		if (!districts.containsKey(distId)) {
			return 0;
		}
		int count = 0;
		ChessDistrict d = (ChessDistrict) districts.get(distId);
		Iterator<Entry<Long, Long>> it = npcRoom.entrySet().iterator();

		while (it.hasNext()) {
			Entry<Long, Long> en = it.next();
			IChessRoom chessRoom = d.district.get(en.getValue());//
			if (chessRoom != null) {
				ChessUtils.chessLog.info(" npcClean:" + en.getKey() + "_" + en.getValue());
				d.leavingCheck(en.getKey(), chessRoom);
				npcRoom.remove(en.getKey());
				count++;
			}
		}
		return count;
	}



	/**
	 * 动态建区 比赛区
	 * @param districtId
	 * @return
	 */
	public synchronized int constructDistrictMatchId(int districtId) {
		if (containsDistrictId(districtId)) {
			return districtId;
		} else {
			StaticMatch staticMatch = StaticMatch.get(districtId);
			if (null == staticMatch) {
				return 0;
			} else {
				ChessDistrictMatch d = new ChessDistrictMatch(scheduleManager, districtId);
				districts.put(districtId, d);
				d.start();
				return districtId;
			}
		}
	}

	public void enrollMatch(int districtId, ChessPlayer player) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.enrollMatch(player);
		}
	}

	public void enterMatch(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.enterMatch(playerId);
		}
	}
	
	public void cleanMatch(int districtId, long chessRoomId) {
		// 不存在该区
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.cleanMatch(chessRoomId);
		}	
	}
	
	public void updateMatch(int districtId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.updateState();
		}
	}
	
	
	public int putBackInMatchQueue(ChessPlayer player){
		ChessPlayerEx p;
		if(playerRoom.containsKey(player.id)){
			p = playerRoom.get(player.id);
		}else{
			ChessUtils.chessLog.info("putBackInMatchQueue玩家没进入此区\n\n\n");
			return 0;
		}
		
		if (containsDistrictId(p.districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(p.districtId);
			ChessRoomMatch chessRoom = (ChessRoomMatch) getIChessRoom(player);
			if(null != chessRoom ){
				if(chessRoom.isIdle()){
					return d.putBackInQueue(player.id,chessRoom);
				}
			}
		}
		return 0;
	}

	public int getMatchPlayerRank(long playerId) {
		int districtId = getDistrict(playerId);
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getPlayerRank(playerId);
		}
		return 0;
	}
	
	public int getMatchNum(int districtId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getEnrollNum();
		}
		return 0;
	}

	public int getMatchRate(int districtId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getRate();
		}
		return 0;
	}
	
	public int getMatchRank(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getPlayerDynRank(playerId);
		}
		return 0;
	}

	public ChessPlayerMatchRankListDto getMatchFinalRank(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getFinalRank(playerId);
		}
		return null;
	}
	
	public RoomUserMatchPrizeDto getMatchPrize(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getPrize(playerId);
		}
		return null;		
	}
	
	public int getMatchState(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getState(playerId);
		}
		return 0;
	}
	
	public boolean isMatchPrizeAvail(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.isPrizeAvail(playerId);
		}
		return false;
	}

	public int getMatchCounter(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.getCounter(playerId);
		}	
		return 0;
	}
	
	public void setMatchStart(int districtId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.setStateStart();
		}
	}
	
	public void setMatchAbsent(int districtId, long playerId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.setAbsentSyn(playerId);
		}
	}
	
	public int updateMatchCounter(int districtId, long playerId,int counter,int score) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			return d.updateCounter(playerId, counter,score);
		}
		return 0;
	}
	
	public void updateMatchFinalsRank(int districtId) {
		if (containsDistrictId(districtId)) {
			ChessDistrictMatch d = (ChessDistrictMatch) districts.get(districtId);
			d.updateRank();
		}
	}
	
	public void matchNpcAdd(int distId, long playerId) {
		ChessUtils.chessLog.info("========matchNpcAdd:");
		// 不存在该区
		if (constructDistrictMatchId(distId) == 0) {
			throw new GeneralException(ChessErrorCodes.CHANNEL_NOT_EXIST);
		}
		ChessDistrictMatch d;

		// 是否存在 此区
		if (districts.containsKey(distId)) {
			d = (ChessDistrictMatch) districts.get(distId);
			d.addNpc(playerId);
		}
	}




	

}
