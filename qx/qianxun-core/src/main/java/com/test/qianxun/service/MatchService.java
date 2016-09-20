package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.Query;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

import com.test.qianxun.model.Match;


@Service
public class MatchService extends SqlService<Match, Long> {
	private static final String TABLE = Match.class.getAnnotation(Table.class).value();
	private static final String SQL_ALL = String.format("select * from %s ORDER BY id DESC LIMIT ? OFFSET ?",
			TABLE);
	private static final String SQL_SEL = String.format("select * from %s where uid = ? LIMIT ? OFFSET ?",
			TABLE);
	private static final String SQL_COUNT = String.format("select count(*) from %s where uid = ?",
			TABLE);
	public int countByUid(long uid){
		return sqlTemplate.queryForInt(SQL_COUNT, uid);
	}
	
	public List<Match> listByUid(long uid, Page page) {
		Object[] objs = {uid, page.getLimit(), page.getOffset() };
		//Query<LoggerMatchPersist> query = this.query();
		//query.count();
		//int total = query.toInt(new Object[]{});
		int total = countByUid(uid);
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_SEL, Match.class, objs);
	}
	public List<Match> listAll(Page page) {
		Object[] objs = { page.getLimit(), page.getOffset() };
		Query<Match> query = this.query();
		query.count();
		int total = query.toInt(new Object[]{});
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_ALL, Match.class, objs);
	}
}
