package com.chitu.poker.arena;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.persist.GenericDao;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;
import cn.gecko.persist.jdbc.JdbcGenericDao;

/**
 * 今日排名
 * @author open
 *
 */
@Entity
@Table(name = "poker_today_rank")
@PersistEntity(cache = false)
public class PersistTodayRank extends PersistObject {
	
	public static final byte REPEART_REWARD_VERSION_1 = 1;
	public static final byte REPEART_REWARD_VERSION = REPEART_REWARD_VERSION_1;

	private long id;
	
	private int winCount;
	
	private int repeatWin;
	
	private int arenaId;
	
	private byte[] activeRepeatReward;
	
	private byte[] obtainRepeatReward;
	
	public static PersistTodayRank get(long id) {
		return PersistObject.get(PersistTodayRank.class, id);
	}
	
	public static int getWorldRank(int winCount){
		GenericDao genericDao = SpringUtils.getBeanOfType(GenericDao.class);
		int rank = genericDao.count(PersistTodayRank.class, " WHERE winCount > ? ", winCount);
		return rank;
	}
	
	public static int getArenaRank(int arenaId,int winCount){
		GenericDao genericDao = SpringUtils.getBeanOfType(GenericDao.class);
		int rank = genericDao.count(PersistTodayRank.class, " WHERE arenaId = ? AND winCount > ? ", arenaId, winCount);
		return rank;
	}
	
	public static List<PersistTodayRank> getWorldRanks(int size){
		GenericDao genericDao = SpringUtils.getBeanOfType(GenericDao.class);
		List<PersistTodayRank> list = genericDao.getAll(PersistTodayRank.class, size, "ORDER BY winCount DESC");
		return list;
	}
	
	public static List<PersistTodayRank> getArenaRanks(int arenaId, int size){
		GenericDao genericDao = SpringUtils.getBeanOfType(GenericDao.class);
		List<PersistTodayRank> list = genericDao.getAll(PersistTodayRank.class, size, " WHERE arenaId=? ORDER BY winCount DESC");
		return list;
	}
	
	public static PersistTodayRank getWorldFristRank(){
		List<PersistTodayRank> list = getWorldRanks(1);
		if(list.size() > 0){
			list.get(0);
		}
		return null;
	}
	
	/**
	 * 往昨日排名表注入数据
	 */
	public static void copyTable(){
		String sql = "INSERT INTO poker_yesterday_rank(id,winCount) SELECT id,winCount FROM poker_today_rank";
		JdbcGenericDao jdbcGenericDao = (JdbcGenericDao) SpringUtils.getBeanOfType(GenericDao.class);
		jdbcGenericDao.getSimpleJdbcTemplate().update(sql);
	}
	
	/**
	 * 清空表
	 */
	public static void truncateTable(){
		String sql = "TRUNCATE TABLE  poker_today_rank";
		JdbcGenericDao jdbcGenericDao = (JdbcGenericDao) SpringUtils.getBeanOfType(GenericDao.class);
		jdbcGenericDao.getSimpleJdbcTemplate().update(sql);
	}
	
	
	
	public static List<Integer> initReward(byte[] byteData){
		List<Integer> list = new ArrayList<Integer>();
		if (byteData == null || byteData.length == 0) {
			return list;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		byte version = buffer.get();
		if (version == REPEART_REWARD_VERSION_1) {
			int size = buffer.getInt();
			for(int i=0;i<size;i++){
				list.add(buffer.getInt());
			}
		}
		return list;
	}
	
	public static byte[] repeatData(List<Integer> rewards) {
		int length = 1 + 4 + rewards.size() * 4;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(REPEART_REWARD_VERSION);
		buffer.putInt(rewards.size());
		for(int rewardId : rewards){
			buffer.putInt(rewardId);
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}
	
	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getRepeatWin() {
		return repeatWin;
	}

	public void setRepeatWin(int repeatWin) {
		this.repeatWin = repeatWin;
	}

	public int getArenaId() {
		return arenaId;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}

	public byte[] getActiveRepeatReward() {
		return activeRepeatReward;
	}

	public void setActiveRepeatReward(byte[] activeRepeatReward) {
		this.activeRepeatReward = activeRepeatReward;
	}

	public byte[] getObtainRepeatReward() {
		return obtainRepeatReward;
	}

	public void setObtainRepeatReward(byte[] obtainRepeatReward) {
		this.obtainRepeatReward = obtainRepeatReward;
	}

	

	
}
