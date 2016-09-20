package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.Query;
import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.test.qianxun.model.Exchange;

@Service
@Transactional
public class ExchangeService extends SqlService<Exchange, Long> {		
	@Autowired
	private UserService userService;
	private static final String TABLE = Exchange.class.getAnnotation(Table.class).value();
	private static final String SQL_ALL = String.format("select * from %s ORDER BY id DESC LIMIT ? OFFSET ?",
			TABLE);
	private static final String SQL_SEL = String.format("select * from %s where uid = ? order by id desc LIMIT ? OFFSET ?",
			TABLE);
	private static final String SQL_COUNT = String.format("select count(*) from %s where uid = ?",
			TABLE);
	private static final String SQL_UPDATE = String.format("update %s set uid=?,invoice=?,remark=? where id = ?",
			TABLE);
	
	@Autowired
	private PlatformTransactionManager txManager;

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	public int countByUid(long uid){
		return sqlTemplate.queryForInt(SQL_COUNT, uid);
	}

	public List<Exchange> listByUid(long uid, Page page) {
		Object[] objs = {uid, page.getLimit(), page.getOffset() };
		int total = countByUid(uid);
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_SEL, Exchange.class, objs);
	}
	public List<Exchange> listAll(Page page) {
		Object[] objs = { page.getLimit(), page.getOffset() };
		Query<Exchange> query = this.query();
		query.count();
		int total = query.toInt(new Object[]{});
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_ALL, Exchange.class, objs);
	}

	public int updateInvoice(long id,long uid, String invoice, String remark) {
		return sqlTemplate.updateArray(SQL_UPDATE,uid,invoice,remark,id);
	}
	
	public void saveWithTransaction(Exchange ex){
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		try {
			save(ex);
			txManager.commit(status);
		} catch (DataAccessException e) {
			txManager.rollback(status);
			throw e;
		}
	}
}