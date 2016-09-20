package com.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.Query;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.LoggerPersist;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LoggerPersistService extends SqlService<LoggerPersist, Long> {
	private static final String TABLE = LoggerPersist.class.getAnnotation(Table.class).value();
	private static final String SQL_SEL = String.format("select * from %s ORDER BY id DESC LIMIT ? OFFSET ?",
			TABLE);
	private static final String SQL_SEL_UID = String.format("select * from %s where uid=? AND type=? ORDER BY id DESC LIMIT ? OFFSET ?",
			TABLE);
	private static final String SQL_SEL_UID_COUNT = " where uid=? AND type=?";

	public List<LoggerPersist> listExchangByUid(long uid, int type, Page page) {
		Object[] objs = { uid, type, page.getLimit(), page.getOffset() };
		Query<LoggerPersist> query = this.query();
		query.count().set(SQL_SEL_UID_COUNT);
		int total = query.toInt(uid, type);
		//query.count();
		//int total = query.toInt(new Object[]{});
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_SEL_UID, LoggerPersist.class, objs);
	}
	
	public List<LoggerPersist> listExchang(Page page) {
		Object[] objs = { page.getLimit(), page.getOffset() };
		Query<LoggerPersist> query = this.query();
		query.count();
		int total = query.toInt(new Object[]{});
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_SEL, LoggerPersist.class, objs);
	}
}
