/**
 * 
 */
package com.chitu.chess.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chitu.chess.msg.RoomUserResultReplayDto;
import com.chitu.chess.msg.RoomUserShowCardReplayDto;
import com.chitu.chess.service.ChessPlayerManager;

import cn.gecko.commons.model.Pageable;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.persist.GenericDao;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

/**
 * @author ivan
 * 
 */
@Entity
@Table(name = "chess_player")
@PersistEntity(cache = false)
public class PersistChessPlayer extends PersistObject {

	public static final byte MISSION_VERSION_1 = 1;
	public static final byte MISSION_VERSION = MISSION_VERSION_1;

	public static final byte ACHIEVEMENT_VERSION_1 = 1;
	public static final byte ACHIEVEMENT_VERSION = ACHIEVEMENT_VERSION_1;
	
	public static final byte REPLAY_VERSION_1 = 1;
	public static final byte REPLAY_VERSION = REPLAY_VERSION_1;

	public static final byte PAYMENT_VERSION_1 = 1;
	public static final byte PAYMENT_VERSION = PAYMENT_VERSION_1;
	
	public static Pageable<PersistChessPlayer> find(int pageNum, int pageSize, String where) {
		return SpringUtils.getBeanOfType(GenericDao.class).find(pageNum, pageSize, PersistChessPlayer.class, " where id>?" + where, 0);
	}

	public static PersistChessPlayer get(long id) {
		return PersistChessPlayer.get(PersistChessPlayer.class, id);
	}

	public static PersistChessPlayer getByAccountId(String accountId) {
		return SpringUtils.getBeanOfType(GenericDao.class).get(PersistChessPlayer.class, " where accountId=?", accountId);
	}

	public static PersistChessPlayer getByNickname(String nickname) {
		return SpringUtils.getBeanOfType(GenericDao.class).get(PersistChessPlayer.class, " where nickname=?", nickname);
	}
	
	public static int getAmount() {
		return SpringUtils.getBeanOfType(GenericDao.class).count(PersistChessPlayer.class, "");
	}

	@Override
	public Long id() {
		return id;
	}

	private long id;

	private String password;
	
	private String salt;
	
	private String accountId;

	private String nickname;
	
	private String loginIp;
	
	private String registryIp;//record ip when enroll

	private int npc;
	
	private int grade;

	private int gender;
	
	private int avatar;

	private long createTime;

	private long loginTime;

	private int onlineTime;
	
	private int loginCount;

	private int battleId;

	private int money;

	private int point;

	private int prestige;
	
	private int rmb;

	private long flags;

	private int exp = 0;

	private int victoryAmount = 0;
	private int gameAmount = 0;
	private int continuousVictory = 0;
	private int continuousVictoryMax = 0;
	private int victoryAmountEachday = 0;
	private int gameAmountEachday = 0;

	private byte[] mission;
	private byte[] achievement;
	private byte[] replay;
	
	// buff
	private long buffVip = 0;
	private long buffSafebox = 0;
	private long buffExpression = 0;
	private long buffVipExpression = 0;
	private byte[] buffLoginMoneyGiving; // Map<Integer, Long>
											// buffLoginMoneyGiving = new
											// HashMap<Integer,
											// Long>();//<金钱,buff时间>
	private long buffMultiplePoint;// <倍数,buff时间>
	private int buffMultiplePointVal = 0;
	// times
	private int speaker = 0;
	// items

	// fields below not included in db
	public boolean firstLogin = false;
	public long lastLoginTime = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount += loginCount;
	}

	public long getFlags() {
		return flags;
	}

	public void setFlags(long flags) {
		this.flags = flags;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getPrestige() {
		return prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public int getRmb() {
		return rmb;
	}

	public void setRmb(int rmb) {
		this.rmb = rmb;
	}

	public int getVictoryAmount() {
		return victoryAmount;
	}

	public void setVictoryAmount(int victoryAmount) {
		this.victoryAmount = victoryAmount;
	}

	public int getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(int gameAmount) {
		this.gameAmount = gameAmount;
	}

	public int getVictoryAmountEachday() {
		return victoryAmountEachday;
	}

	public void setVictoryAmountEachday(int victoryAmountEachday) {
		this.victoryAmountEachday = victoryAmountEachday;
	}

	public int getGameAmountEachday() {
		return gameAmountEachday;
	}

	public void setGameAmountEachday(int gameAmountEachday) {
		this.gameAmountEachday = gameAmountEachday;
	}

	public int getContinuousVictory() {
		return continuousVictory;
	}

	public void setContinuousVictory(int continuousVictory) {
		this.continuousVictory = continuousVictory;
	}

	public byte[] getMission() {
		return mission;
	}

	public void setMission(byte[] mission) {
		this.mission = mission;
	}

	public byte[] getAchievement() {
		return achievement;
	}

	public void setAchievement(byte[] achievement) {
		this.achievement = achievement;
	}

	public int getContinuousVictoryMax() {
		return continuousVictoryMax;
	}

	public void setContinuousVictoryMax(int continuousVictoryMax) {
		this.continuousVictoryMax = continuousVictoryMax;
	}

	public long getBuffVip() {
		return buffVip;
	}

	public void setBuffVip(long buffVip) {
		this.buffVip = buffVip;
	}

	public long getBuffSafebox() {
		return buffSafebox;
	}

	public void setBuffSafebox(long buffSafebox) {
		this.buffSafebox = buffSafebox;
	}

	public long getBuffExpression() {
		return buffExpression;
	}

	public void setBuffExpression(long buffExpression) {
		this.buffExpression = buffExpression;
	}

	public long getBuffVipExpression() {
		return buffVipExpression;
	}

	public void setBuffVipExpression(long buffVipExpression) {
		this.buffVipExpression = buffVipExpression;
	}

	public byte[] getBuffLoginMoneyGiving() {
		return buffLoginMoneyGiving;
	}

	public void setBuffLoginMoneyGiving(byte[] buffLoginMoneyGiving) {
		this.buffLoginMoneyGiving = buffLoginMoneyGiving;
	}

	public long getBuffMultiplePoint() {
		return buffMultiplePoint;
	}

	public void setBuffMultiplePoint(long buffMultiplePoint) {
		this.buffMultiplePoint = buffMultiplePoint;
	}

	public int getBuffMultiplePointVal() {
		return buffMultiplePointVal;
	}

	public void setBuffMultiplePointVal(int buffMultiplePointVal) {
		this.buffMultiplePointVal = buffMultiplePointVal;
	}

	public int getSpeaker() {
		return speaker;
	}

	public void setSpeaker(int speaker) {
		this.speaker = speaker;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String ip) {
		this.loginIp = ip;
	}

	public String getRegistryIp() {
		return registryIp;
	}

	public void setRegistryIp(String registryIp) {
		this.registryIp = registryIp;
	}

	public int getNpc() {
		return npc;
	}

	public void setNpc(int npc) {
		this.npc = npc;
	}

	public byte[] getReplay() {
		return replay;
	}

	public void setReplay(byte[] replay) {
		this.replay = replay;
	}

	public void initMission(Map<Integer, Integer> map) {
		if (mission == null || mission.length == 0) {
			return;
		}
		ByteBuffer buffer = ByteBuffer.wrap(mission);
		byte version = buffer.get();
		if (version == MISSION_VERSION) {
			int size = buffer.getInt();
			for (int i = 0; i < size; i++) {
				int key = buffer.getInt();
				int value = buffer.getInt();
				map.put(key, value);
			}
		}
	}

	public void MissionSerialization(Map<Integer, Integer> map) {
		int length = 1 + 4 + map.size() * (4 + 4);// 版本号 +map长度
															// +map长度*两个int长度
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(MISSION_VERSION);
		buffer.putInt(map.size());

		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			buffer.putInt(entry.getKey());
			buffer.putInt(entry.getValue());
		}
		mission = buffer.array();
		// chessLog.info("bytesLen:"+bytes.length);
	}

	public void initAchievement(Map<Integer, Integer> map) {
		if (achievement == null || achievement.length == 0) {
			return;
		}
		ByteBuffer buffer = ByteBuffer.wrap(achievement);
		byte version = buffer.get();
		if (version == ACHIEVEMENT_VERSION) {
			int size = buffer.getInt();
			for (int i = 0; i < size; i++) {
				int key = buffer.getInt();
				int value = buffer.getInt();
				map.put(key, value);
			}
		}
	}

	public void achievementSerialization(Map<Integer, Integer> map) {
		int length = 1 + 4 + map.size() * (4 + 4);// 版本号 +map长度
																// +map长度*两个int长度
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(ACHIEVEMENT_VERSION);
		buffer.putInt(map.size());

		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			buffer.putInt(entry.getKey());
			buffer.putInt(entry.getValue());
		}
		achievement = buffer.array();
		// chessLog.info("bytesLen:"+bytes.length);
	}
	
	public RoomUserResultReplayDto initReplay() {
		if (replay == null || replay.length == 0) {
			return null;
		}
		ChessUtils.chessLog.info("bytesLen:"+replay.length);
		ByteBuffer buffer = ByteBuffer.wrap(replay);
		byte version = buffer.get();
		if (version == REPLAY_VERSION) {		
			List<RoomUserShowCardReplayDto> list = new ArrayList<RoomUserShowCardReplayDto>();
			ChessPlayerManager chessPlayerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
			for (int i = 0; i < ChessCardTypes.PLAYERNUM; i++) {
				
				long playerId = buffer.getLong();ChessUtils.chessLog.info("playerId:"+playerId);		
				int[] fst	= new int[3];
				int[] sec	= new int[5];
				int[] thd	= new int[5];
				//玩家出牌-int[] * 4
				for(int j=0; j<3; j++){
					fst[j] = buffer.getInt();
				}
				for(int j=0; j<5; j++){
					sec[j] = buffer.getInt();
				}
				for(int j=0; j<5; j++){
					thd[j] = buffer.getInt();
				}		
				int point = buffer.getInt();
				int money = buffer.getInt();
				int special = buffer.getInt();
				if(playerId > 0){
					ChessPlayer tmpPlayer = chessPlayerManager.getAnyPlayerById(playerId);
					RoomUserShowCardReplayDto rp = new RoomUserShowCardReplayDto();
					rp.setFirstCard(fst);
					rp.setSecondCard(sec);
					rp.setThirdCard(thd);
					rp.setPoint(point);
					rp.setMoney(money);
					rp.setPlayerId(String.valueOf(playerId));
					rp.setSpecialType(special);
					rp.setNickname(tmpPlayer.nickname);
					list.add(rp);
				}			
			}
			return new RoomUserResultReplayDto(list);
		}
		return null;
	}

	public static byte[] ReplaySerialization(ChessRoom chessRoom) {
		int length = 1 + 8*4 + 13*4*4 + 4*4 + 4*4 + 4*4;// 版本号 +长度
		ByteBuffer buffer = ByteBuffer.allocate(length);
		byte[] bytes;
		buffer.put(REPLAY_VERSION);
		
		for(int i = 0; i<chessRoom.chessRoomPlayer.length; i++){
			//玩家id-long * 4
			if((chessRoom.chessRoomPlayer[i].playerState.state & ChessRoomPlayer.PlayerStates.SHOWED.state) > 0){
				buffer.putLong(chessRoom.chessRoomPlayer[i].playerId);
			}else{
				buffer.putLong(0);
			}		
			
			//玩家出牌-int[] * 4
			for(int j=0; j<chessRoom.chessRoomPlayer[i].playerCardSequence1.length; j++){
				buffer.putInt(chessRoom.chessRoomPlayer[i].playerCardSequence1[j]);
			}
			for(int j=0; j<chessRoom.chessRoomPlayer[i].playerCardSequence2.length; j++){
				buffer.putInt(chessRoom.chessRoomPlayer[i].playerCardSequence2[j]);
			}
			for(int j=0; j<chessRoom.chessRoomPlayer[i].playerCardSequence3.length; j++){
				buffer.putInt(chessRoom.chessRoomPlayer[i].playerCardSequence3[j]);
			}
			
			//玩家得分 -int * 4
			buffer.putInt(chessRoom.chessRoomPlayer[i].cardScoreSum);
			
			//玩家获得金币-int * 4
			buffer.putInt(chessRoom.chessRoomPlayer[i].cardMoneySum);
			
			//玩家获得特殊牌-int * 4
			buffer.putInt(chessRoom.chessRoomPlayer[i].cardSpecialType);
		}
		
		bytes = buffer.array();
		return bytes;
	}

}
