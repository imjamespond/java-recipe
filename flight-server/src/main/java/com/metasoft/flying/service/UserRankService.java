package com.metasoft.flying.service;

import java.util.List;

import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Component;

import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.UserRankPersist;

@Component
public class UserRankService extends SqlService<UserRankPersist, Long> {
	//private static final Logger logger =  LoggerFactory.getLogger(UserRankPersistenceService.class);
	
	private static final String TABLE = UserRankPersist.class.getAnnotation(Table.class).value();
	private static final String TABLE2 = UserPersist.class.getAnnotation(Table.class).value();
	//private static final String TABLE3 = "web_users";
	/*
	private static final String SQL_SEL_CHARM = String
//			.format("select a.*,b.nickname AS username,b.id AS fuid from %s AS a"
//					+ " RIGHT JOIN %s AS b ON a.uid = b.id"
//					+ " WHERE b.gender = 2 AND (a.weeknum=? or a.weeknum is null) ORDER BY a.charm DESC LIMIT ? OFFSET ?",TABLE, TABLE2);
//			.format("select b.id AS fuid, sum(a.charm) as sumnum from %s AS a"
//					+ " RIGHT JOIN %s AS b ON a.uid = b.id"
//					+ " WHERE b.gender = 2 AND (a.weeknum=? or a.weeknum is null) GROUP BY b.id ORDER BY sumnum DESC LIMIT ? OFFSET ?", TABLE, TABLE2);			
			.format("select b.id as fuid,b.nickname as username,c.sumnum from %s as b "
					+ "left join (select a.uid, sum(a.charm) as sumnum from %s as a WHERE a.weeknum=? GROUP BY a.uid) as c on b.id = c.uid WHERE b.gender = 2 ORDER BY c.sumnum DESC NULLS LAST LIMIT ? OFFSET ?", TABLE2, TABLE);				

	private static final String SQL_SEL_CONTR = String
//			.format("select a.*,b.nickname AS username from %s AS a"
//					+ " LEFT JOIN %s AS b ON a.id = b.id"
//					+ " WHERE a.weeknum=? AND a.contribute>0 ORDER BY a.contribute DESC LIMIT ? OFFSET ?", TABLE, TABLE2);
			.format("select a.uid AS fuid, sum(a.contribute) as sumnum from %s AS a"
					+ " LEFT JOIN %s AS b ON a.uid = b.id"
					+ " WHERE a.weeknum=? AND a.contribute>0 GROUP BY a.uid ORDER BY sumnum DESC LIMIT ? OFFSET ?", TABLE, TABLE2);
	private static final String SQL_SEL_APPLE_WEEK = String
			.format("select a.*,b.nickname AS username from %s AS a"
					+ " LEFT JOIN %s AS b ON a.uid = b.id"
					+ " LEFT JOIN %s AS c ON a.uid = c.id"
					+ " WHERE a.weeknum=? AND a.apple_in>0 AND (c.applestate & 1 ) = 0 ORDER BY a.apple_in DESC LIMIT ? OFFSET ?", TABLE, TABLE2, TABLE3);
	private static final String SQL_SEL_APPLE_MON = String
			.format("select a.uid, sum(a.apple_in) as sumnum from %s AS a"
					+ " LEFT JOIN %s AS c ON a.uid = c.id"
					+ " WHERE a.monthnum=? AND a.apple_in>0 AND (c.applestate & 2 ) = 0 GROUP BY a.uid ORDER BY sumnum DESC LIMIT ? OFFSET ?", TABLE, TABLE3);
	private static final String SQL_SEL_APPLE_PRIZE = String
//			.format("select a.uid, sum(a.apple_in) as sumnum from %s AS a"
//					+ " LEFT JOIN %s AS c ON a.uid = c.id"
//					+ " WHERE a.prizeid=? AND a.apple_in>0 AND ((c.applestate & 8 = 0) or c.applestate is null) GROUP BY a.uid ORDER BY sumnum DESC LIMIT ? OFFSET ?", TABLE, TABLE3);
			.format("select a.uid, sum(a.apple_in) as sumnum from %s AS a"
					+ " LEFT JOIN %s AS c ON a.uid = c.id"
					+ " WHERE a.prizeid=? AND a.apple_in>0 AND (c.applestate & 8 ) = 0 GROUP BY a.uid ORDER BY sumnum DESC LIMIT ? OFFSET ?", TABLE, TABLE2);
*/
	private static final String SQL_SEL_MATCH_DAY = String
			.format("select a.uid, a.elapsed, a.match, b.nickname as username from %s AS a"
					+ " LEFT JOIN %s AS b ON a.uid = b.id"
					+ " WHERE a.datenum=? AND a.match>0 ORDER BY a.match DESC, elapsed ASC LIMIT ? OFFSET ?", TABLE, TABLE2);
	private static final String SQL_SEL_MATCH_WEEK = String
			.format("select a.uid, sum(a.match) as sumnum, sum(a.elapsed) as elapsed from %s AS a"
					+ " LEFT JOIN %s AS b ON a.uid = b.id"
					+ " WHERE a.weeknum=? AND a.match>0 GROUP BY a.uid ORDER BY sumnum DESC, elapsed ASC LIMIT ? OFFSET ?", TABLE, TABLE2);
	private static final String SQL_SEL_MATCH_MONTH = String
			.format("select b.id,b.score,c.* from (select a.uid, sum(a.match) as sumnum from %s AS a"
					+ " WHERE a.monthnum=? AND a.match>0 GROUP BY a.uid ) as c"
					+ " LEFT JOIN %s AS b ON c.uid = b.id ORDER BY c.sumnum DESC, b.score DESC LIMIT ? OFFSET ?", TABLE, TABLE2);
	
	private static final String SQL_SEL_DAY = String.format(
			"select a.* from %s AS a WHERE a.uid=? AND a.datenum=? ", TABLE);
	private static final String SQL_SEL = String.format(
			"select a.* from %s AS a WHERE a.uid=? AND a.prizeid=? AND a.weeknum=? AND a.yearnum=?", TABLE);
	//private static final String SQL_DEL = String.format("delete from %s WHERE weeknum<(?-10)", TABLE);
	//private static final String SQL_DEL2 = String.format("delete from %s WHERE yearnum<? AND ?>10", TABLE);//今年超过3周后删除去年的记录
/*
	public List<UserRankPersist> getFemaleByRoseDesc(int offset, int size, int weeknum) {
		Object[] objs = { weeknum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_CHARM, UserRankPersist.class, objs);
	}
	public List<UserRankPersist> getByContributeDesc(int offset, int size, int weeknum) {
		Object[] objs = { weeknum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_CONTR, UserRankPersist.class, objs);
	}	
	public List<UserRankPersist> getByAppleDesc(int offset, int size, int weeknum) {
		Object[] objs = { weeknum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_APPLE_WEEK, UserRankPersist.class, objs);
	}
	public List<UserRankPersist> getByAppleDesc2(int offset, int size, int monthnum) {
		Object[] objs = { monthnum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_APPLE_MON, UserRankPersist.class, objs);
	}*/
	public UserRankPersist getCurrentDayRank(long id, int epochDay) {
		Object[] objs = { id, epochDay};
		List<UserRankPersist> list = this.sqlTemplate.queryForList(SQL_SEL_DAY, UserRankPersist.class, objs);
		if (null != list && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	public UserRankPersist getCurrentWeekRank(long id, int prizeid, int week, int year) {
		Object[] objs = { id, prizeid, week, year };
		List<UserRankPersist> list = this.sqlTemplate.queryForList(SQL_SEL, UserRankPersist.class, objs);
		if (null != list && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	/*
	public List<UserRankPersist> getByApplePrizeDesc(int offset, int size, int prizeId) {
		Object[] objs = { prizeId, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_APPLE_PRIZE, UserRankPersist.class, objs);
	}
	*/
	public List<UserRankPersist> getByMatchByDayDesc(int offset, int size, int day) {
		Object[] objs = { day, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_MATCH_DAY, UserRankPersist.class, objs);
	}
	public List<UserRankPersist> getByMatchByWeekDesc(int offset, int size, int weeknum) {
		Object[] objs = { weeknum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_MATCH_WEEK, UserRankPersist.class, objs);
	}
	public List<UserRankPersist> getByMatchByMonthDesc(int offset, int size, int month) {
		Object[] objs = { month, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_MATCH_MONTH, UserRankPersist.class, objs);
	}
	
	public void clean() {
		//Calendar c = Calendar.getInstance();
		//int year = c.get(Calendar.YEAR);
		//int week = c.get(Calendar.WEEK_OF_YEAR);
		//Object[] objs = { week };
		//this.sqlTemplate.deleteArray(SQL_DEL, objs);
		//Object[] objs2 = { year, week };
		//this.sqlTemplate.deleteArray(SQL_DEL2, objs2);
	}


}
