package com.metasoft.service.dao;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.metasoft.dao.model.FoobarDao;
import com.metasoft.util.PsqlDaoService;
import com.metasoft.util.PsqlHelper;
 
@Service
public class FoobarDaoService extends PsqlDaoService<FoobarDao> {
	static Logger log = LoggerFactory.getLogger(FoobarDaoService.class);
	@Value("${init.db}")
	Boolean initdb;
	
	@PostConstruct
	public void init(){
		super.init();
		if(initdb){
			createTable();
			createSeq();
			createIndex();
		}
	}

	public void insertFormatAutoId(FoobarDao dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//long id = nextval();
		//dao.setId(Commons.GetInvoiceDate(System.currentTimeMillis())+String.valueOf(id));
    	this.insert(dao);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void testTransation() throws Exception{
		this.insertByAutoId(new FoobarDao("foo","0"));
		this.getJdbcTemplate().execute("create table sd_foobar ()");
	}
	
	public void testOnConflict(FoobarDao dao) throws Exception{
    	Assert.notNull(kInsertByAutoIdSql,"自增字段为空");
		final String sql = PsqlHelper.InsertOnConflictSql(
				FoobarDao.class, kInsertByAutoIdSql, "username");
		log.debug("sql: {}",sql);
		
    	Object[] parameters = getParamsWithoutId(dao);
    	this.getJdbcTemplate().update(sql, parameters);
	}
}
