package com.metasoft.flying.service;

import java.util.List;

import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.SqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.UserWealthPersist;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserWealthService extends SqlService<UserWealthPersist, Long> {
	private static final Logger logger =  LoggerFactory.getLogger(UserWealthService.class);
	
	private static final String TABLE = UserWealthPersist.class.getAnnotation(Table.class).value();
	private static final String TABLE3 = "web_users";
	
	private static final String SQL_ADD_ROSE = String.format("update %s set rose=rose+? where id=?", TABLE);
	private static final String SQL_ADD_CREDIT = String.format("update %s set credit=credit+? where id=?", TABLE);
	private static final String SQL_REDUCE_ROSE = String.format("update %s set rose=rose-? where id=? and rose>=?",
			TABLE);
	private static final String SQL_ADD_GEMS = String.format("update %s set germ=germ+? where id=?", TABLE);
	private static final String SQL_REDUCE_GEMS = String.format("update %s set germ=germ-? where id=? and germ>=?",
			TABLE);

	private static final String SQL_SEL_ROSE = String.format(
			"select * from %s WHERE gender = 2 AND charm>0 ORDER BY charm DESC LIMIT ? OFFSET ?", TABLE);
	private static final String SQL_SEL_APPLE = String.format(
			"select a.* from %s AS a LEFT JOIN %s AS c ON a.id = c.id WHERE a.apple>0 AND (c.applestate & 1 ) = 0 ORDER BY apple DESC LIMIT ? OFFSET ?", TABLE, TABLE3);
	@Autowired
	private PlatformTransactionManager txManager;

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}


	public List<UserWealthPersist> getFemaleByRoseDesc(int offset, int size) {
		Object[] objs = { size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_ROSE, UserWealthPersist.class, objs);
	}


	public List<UserPersist> getByAppleDesc(int offset, int size) {
		Object[] objs = { size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_APPLE, UserPersist.class, objs);
	}


	public int addRose(long userId, int rose) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		int count = 0;
		try {
			count = this.sqlTemplate.updateArray(SQL_ADD_ROSE, rose, userId);
			txManager.commit(status);
		} catch (DataAccessException e) {
			logger.warn("addRose rolling back");
			txManager.rollback(status);
			throw e;
		}
		return count;
	}

	public int reduceRose(long userId, int rose) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		int count = 0;
		try {
			count = this.sqlTemplate.updateArray(SQL_REDUCE_ROSE, rose, userId, rose);
			txManager.commit(status);
		} catch (DataAccessException e) {
			logger.warn("reduceRose rolling back");
			txManager.rollback(status);
			throw e;
		}
		return count;
	}
	
	public int addCredit(UserWealthPersist up, int s) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		int count = 0;
		try {
			count = this.sqlTemplate.updateArray(SQL_ADD_CREDIT, s, up.getId());
			UserWealthPersist newup = this.get(up.getId());
			up.setCredit(newup.getCredit());
			txManager.commit(status);
		} catch (DataAccessException e) {
			logger.warn("addCredit rolling back");
			txManager.rollback(status);
			throw e;
		}
		return count;
	}
	
	public int addGems(UserWealthPersist up, int gems) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		int count = 0;
		try {
			count = this.sqlTemplate.updateArray(SQL_ADD_GEMS, gems, up.getId());
			UserWealthPersist newup = this.get(up.getId());
			up.setGems(newup.getGems());
			txManager.commit(status);
		} catch (DataAccessException e) {
			logger.warn("addGems rolling back");
			txManager.rollback(status);
			throw e;
		}
		return count;
	}

	public int reduceGems(UserWealthPersist up, int gems) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		int count = 0;
		try {
			count = this.sqlTemplate.updateArray(SQL_REDUCE_GEMS, gems, up.getId(), gems);
			UserWealthPersist newup = this.get(up.getId());
			up.setGems(newup.getGems());
			txManager.commit(status);
		} catch (DataAccessException e) {
			logger.warn("Error in creating record, rolling back");
			txManager.rollback(status);
			throw e;
		}
		return count;
	}
}
