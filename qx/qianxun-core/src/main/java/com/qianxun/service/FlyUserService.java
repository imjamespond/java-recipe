package com.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.Query;
import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.qianxun.model.FlyUser;
import com.qianxun.service.LoggerPersistService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FlyUserService extends SqlService<FlyUser, Long> {
	@Autowired
	private LoggerPersistService loggerService;
	@Autowired
	private PlatformTransactionManager txManager;
	
	private static final String TABLE = FlyUser.class.getAnnotation(Table.class).value();

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}


	public String getNicknameInGame(long uid) {
		FlyUser fp = this.get(uid);
		if (fp != null) {
			return fp.getNickname();
		}
		return "";
	}

	public FlyUser getByName(String name) {
		return this.sqlTemplate.queryForObject(
				"select * from fly_users where username = ?",
				FlyUser.class, name);
	}


	public List<FlyUser> listByTotaltime(Page page) {
		Object[] objs = {  page.getLimit(), page.getOffset() };
		Query<FlyUser> query = query();
		query.count();
		page.setTotal(query.toInt());
		return sqlTemplate.queryForList(String.format("select * from %s order by totaltime desc limit ? offset ?",TABLE), FlyUser.class, objs);
	}


	public List<FlyUser> listByConsecutive(Page page) {
		Object[] objs = {  page.getLimit(), page.getOffset() };
		Query<FlyUser> query = query();
		query.count();
		page.setTotal(query.toInt());
		return sqlTemplate.queryForList(String.format("select * from %s order by consecutive desc limit ? offset ?",TABLE), FlyUser.class, objs);
	}


	public List<FlyUser> listByTotaldays(Page page) {
		Object[] objs = {  page.getLimit(), page.getOffset() };
		Query<FlyUser> query = query();
		query.count();
		page.setTotal(query.toInt());
		return sqlTemplate.queryForList(String.format("select * from %s order by totaldays desc limit ? offset ?",TABLE), FlyUser.class, objs);
	}


	public List<FlyUser> listByLogindate(Page page) {
		Object[] objs = {  page.getLimit(), page.getOffset() };
		Query<FlyUser> query = query();
		query.count();
		page.setTotal(query.toInt());
		return sqlTemplate.queryForList(String.format("select * from %s order by logindate desc limit ? offset ?",TABLE), FlyUser.class, objs);
	}

}
