package com.chitu.chess.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.Pageable;
import cn.gecko.commons.utils.IdUtils;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.persist.PersistSession;

import com.chitu.chess.data.StaticDistrictTypes;
import com.chitu.chess.data.StaticNPC;
import com.chitu.chess.model.ChessBillTypes;
import com.chitu.chess.service.ChessPlayerManager;
import com.chitu.chess.service.ChessRoomManager;

public class ChessUtils4Web {
	
	public static String getStatistic() {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		
		String r = "";
		Map<Long, Integer> registry = chessPlayerManager.getRegistry();
		Iterator<Entry<Long, Integer>> it = registry.entrySet().iterator();
		while(it.hasNext()){
			Entry<Long, Integer> e = it.next();	
			r += "\""+ e.getKey() +"\":"+e.getValue()+",";			
		}

		String info = "{";
		info += "\"online\":"+chessPlayerManager.getOnlineCount()+",";
		info += "\"amount\":"+PersistChessPlayer.getAmount()+",";
		info += "\"launch\":"+chessPlayerManager.launchpoint+",";
		info += "\"registry\":{"+r+"\"end\":0}";
		info += "}";
		
		return info;
	}

	public static int getDistrictSize(int districtId) {

		//ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);

		return 0;
	}

	public static ChessDistrict getDistrict(int districtId) {
		//是否普通区
		StaticDistrictTypes sdt = StaticDistrictTypes.get(districtId);
		if (null == sdt) {
			return null;
		}
		
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);

		// 不存在该区
		if (!chessRoomManager.districts.containsKey(districtId)) {
			return null;
		}
		
		ChessDistrict d = (ChessDistrict) chessRoomManager.districts.get(districtId);
		return d;
	}

	public static int getDistPlayerNum(int districtId) {

		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);

		// 不存在该区
		if (!chessRoomManager.districts.containsKey(districtId)) {
			return 0;
		}
		return chessRoomManager.getDistPlayerNum(districtId);
	}

	public static ChessRoom getChessRoom(int districtId, String strRoomId) {
		//是否普通区
		StaticDistrictTypes sdt = StaticDistrictTypes.get(districtId);
		if (null == sdt) {
			return null;
		}
		

		long roomId = Long.valueOf(strRoomId);

		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);

		// 不存在该区
		if (!chessRoomManager.districts.containsKey(districtId)) {
			return null;
		}
		ChessDistrict d = (ChessDistrict) chessRoomManager.districts.get(districtId);

		// 不存在该房间
		if (!d.district.containsKey(roomId)) {
			return null;
		}
		ChessRoom room = (ChessRoom) d.district.get(roomId);
		return room;
	}

	public static ChessPlayer getChessPlayer(long playerId) {
		// long playerId = Long.valueOf(strplayerId);
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);

		return playerTmp;
	}
	
	public static void cardSpecify(int num,long playerId,String cardStr) {
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);

		//fix me for npc test
		//long npcRoomId = chessRoomManager.npcRoom.get(playerId);
		//ChessDistrict d = (ChessDistrict) chessRoomManager.districts.get(1001);
		//IChessRoom ic = d.getIChessRoom(npcRoomId);
		
		IChessRoom ic = chessRoomManager.getIChessRoom(playerTmp);
		ic.cardSpecify(num, playerId, cardStr);
	}

	public static int add(long playerId, int money, int point, int rmb) {
		// long playerId = Long.valueOf(strplayerId);
		ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessPlayer playerTmp = chessPlayerManager.getAnyPlayerById(playerId);
		if (rmb > 0) {
			playerTmp.wealthHolder.increaseMoney(0, 0, rmb, BillType.get(ChessBillTypes.GM), "");
			playerTmp.persistAll();
			return playerTmp.wealthHolder.getRmb();
		}
		if (money < 0) {
			playerTmp.wealthHolder.decreaseMoney(Math.abs(money), point, 0, BillType.get(ChessBillTypes.GM), "");
			playerTmp.persistAll();
			return playerTmp.wealthHolder.getMoney();
		}
		if (money > 0) {
			playerTmp.wealthHolder.increaseMoney(Math.abs(money), point, 0, BillType.get(ChessBillTypes.GM), "");
			playerTmp.persistAll();
			return playerTmp.wealthHolder.getMoney();
		}
		return 0;
	}

	public static void matchNpcAdd(int distId,int playerNum,int roomNum) {
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		
		Pageable<PersistChessPlayer> mypage = PersistChessPlayer.find(0,playerNum," and npc = 1 ");//
		List<PersistChessPlayer> list = mypage.getElements();
		Iterator<PersistChessPlayer> itr = list.iterator();

		while (itr.hasNext()) {
			PersistChessPlayer p = itr.next();
			chessRoomManager.matchNpcAdd( distId, p.getId());
			ChessUtils.chessLog.info("========"+p.getAccountId());
		}
	}
	
	public static void createNpcRoom(String strPlayerId) {
		long playerId = Long.valueOf(strPlayerId);
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		chessRoomManager.npcNewRoom( 1001, playerId);
	}
	
	public static int createNpcRooms(int distId,int playerNum,int roomNum) {
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		
		Pageable<PersistChessPlayer> mypage = PersistChessPlayer.find(0,1000," and npc = 1 ");//
		List<PersistChessPlayer> list = mypage.getElements();
		Iterator<PersistChessPlayer> itr = list.iterator();
		int roomNumCount = 0;
		int playerNumCount = 0;
		ChessRoom chessRoom = null;
		while (itr.hasNext()) {
			PersistChessPlayer p = itr.next();
			
			if(playerNumCount == 0){//建房
				if(roomNumCount<roomNum){
					chessRoom = chessRoomManager.npcNewRoom( distId, p.getId());
					if(chessRoom != null){
						playerNumCount++;
						roomNumCount++;
					}
				}
			}else if(playerNumCount < playerNum){//加人			
				if(chessRoom != null){
					chessRoomManager.npcAdd(chessRoom,distId, p.getId());
					playerNumCount++;
				}
			}
			
			if(playerNumCount == playerNum){
				playerNumCount = 0;
			}
		}
		
		return roomNumCount;
	}
	
	public static int cleanNpc(int distId) {
		ChessRoomManager chessRoomManager = SpringUtils.getBeanOfType(ChessRoomManager.class);
		return chessRoomManager.npcClean(distId);
	}
	

	public static int importNpc() {
		ChessUtils.chessLog.info("==========npc========导入");

		int count = 0;
		Map<Integer,StaticNPC> m = StaticNPC.getAll();
		Iterator<Entry<Integer, StaticNPC>> it = m.entrySet().iterator();
		while(it.hasNext()){
			StaticNPC npc = it.next().getValue();
			
			ChessUtils.chessLog.info(npc.getName());
			PersistChessPlayer persistTdPlayer = PersistChessPlayer.getByAccountId(npc.getName());

			if (persistTdPlayer == null) {
				persistTdPlayer = new PersistChessPlayer();
				persistTdPlayer.setId(IdUtils.generateLongId());
				persistTdPlayer.setPassword("QAZWSXEDC");
				persistTdPlayer.setSalt("QAZWSXEDC");
				persistTdPlayer.setAccountId(npc.getName());
				persistTdPlayer.setNickname(npc.getName());
				persistTdPlayer.setRegistryIp("0.0.0.0");
				persistTdPlayer.setNpc(1);
				persistTdPlayer.setMoney(100);
				persistTdPlayer.setExp(100);
				persistTdPlayer.setGender(0);
				persistTdPlayer.setCreateTime(System.currentTimeMillis());
				persistTdPlayer.setLoginTime(System.currentTimeMillis());

				persistTdPlayer.setMission(new byte[1]);
				persistTdPlayer.setAchievement(new byte[1]);// 初始成就
				persistTdPlayer.setReplay(new byte[1]);
				persistTdPlayer.setBuffLoginMoneyGiving(new byte[1]);

				persistTdPlayer.lastLoginTime = System.currentTimeMillis();

				persistTdPlayer.save();
				PersistSession.commit();
				count++;
				// throw new GeneralException(ChessErrorCodes.ROLE_NOT_EXIST);
			} else {
				ChessUtils.chessLog.info("npc已存在:"+npc.getName());
			}
		}
		ChessUtils.chessLog.info("==========npc========导入END\n\n\n");
		return count;
	}
}
