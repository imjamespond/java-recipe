package com.metasoft.flying.service;

import java.util.List;

import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.UserRankPersist;

@Service
public class UserRankPersistenceService extends SqlService<UserRankPersist, Long> {
	private static final String TABLE = UserRankPersist.class.getAnnotation(Table.class).value();
	private static final String TABLE2 = UserPersist.class.getAnnotation(Table.class).value();
	private static final String SQL_SEL_ROSE = String
			.format("select a.*,b.nickname AS username from %s AS a" + " LEFT JOIN %s AS b ON a.id = b.id"
					+ " WHERE b.gender = 2 AND a.weeknum=? AND a.charm>0 ORDER BY a.charm DESC LIMIT ? OFFSET ?",
					TABLE, TABLE2);
	private static final String SQL_SEL_CONTR = String.format("select a.*,b.username AS username from %s AS a"
			+ " LEFT JOIN %s AS b ON a.id = b.id"
			+ " WHERE a.weeknum=? AND a.contribute>0 ORDER BY a.contribute DESC LIMIT ? OFFSET ?", TABLE, TABLE2);

	public List<UserRankPersist> getFemaleByRoseDesc(int offset, int size, int weeknum) {
		Object[] objs = { weeknum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_ROSE, UserRankPersist.class, objs);
	}

	public List<UserRankPersist> getByContributeDesc(int offset, int size, int weeknum) {
		Object[] objs = { weeknum, size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_CONTR, UserRankPersist.class, objs);
	}

}
