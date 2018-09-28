package com.metasoft.empire.service;

import java.util.List;

import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.metasoft.empire.model.UserPersist;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserPersistService extends SqlService<UserPersist, Long> {
	//private static final Logger logger =  LoggerFactory.getLogger(UserPersistService.class);
	
	private static final String TABLE = UserPersist.class.getAnnotation(Table.class).value();
	private static final String SQL_SEL_NAME = String.format("select * from %s where username=?", TABLE);
	private static final String SQL_SEL_NICKNAME = String.format("select * from %s where nickname=?", TABLE);
	public void setTxManager(PlatformTransactionManager txManager) {
	}

	public UserPersist getByName(String name) {
		Object[] objs = { name };
		return this.sqlTemplate.queryForObject(SQL_SEL_NAME, UserPersist.class, objs);
	}
	
	public UserPersist getByUserName(String name) {
		Object[] objs = { name };
		return this.sqlTemplate.queryForObject(SQL_SEL_NAME, UserPersist.class, objs);
	}

	public List<UserPersist> getByNickName(String nickname) {
		Object[] objs = { nickname };
		return this.sqlTemplate.queryForList(SQL_SEL_NICKNAME, UserPersist.class, objs);
	}

}
