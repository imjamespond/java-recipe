package com.chitu.poker.arena;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.persist.GenericDao;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;
import cn.gecko.persist.jdbc.JdbcGenericDao;

/**
 * 昨日排名
 * @author open
 *
 */
@Entity
@Table(name = "poker_yesterday_rank")
@PersistEntity(cache = false)
public class PersistYesterdayRank extends PersistObject {

    private long id;
	
	private int winCount;
	
	private int obtainRankReward;
	
	public static PersistYesterdayRank get(long id) {
		return PersistObject.get(PersistYesterdayRank.class, id);
	}
	
	public static int getWorldRank(int winCount){
		GenericDao genericDao = SpringUtils.getBeanOfType(GenericDao.class);
		int rank = genericDao.count(PersistTodayRank.class, " WHERE winCount > ? ", winCount);
		return rank;
	}
	
	/**
	 * 清空表
	 */
	public static void truncateTable(){
		String sql = "TRUNCATE TABLE  poker_yesterday_rank";
		JdbcGenericDao jdbcGenericDao = (JdbcGenericDao) SpringUtils.getBeanOfType(GenericDao.class);
		jdbcGenericDao.getSimpleJdbcTemplate().update(sql);
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

	public int getObtainRankReward() {
		return obtainRankReward;
	}

	public void setObtainRankReward(int obtainRankReward) {
		this.obtainRankReward = obtainRankReward;
	}
	
	

}
