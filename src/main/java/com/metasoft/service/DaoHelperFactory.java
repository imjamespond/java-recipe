package com.metasoft.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.util.DB2DaoHelper;
import com.metasoft.util.DBType;
import com.metasoft.util.GenericDaoHelper;
import com.metasoft.util.MySqlDaoHelper;

@Service
public class DaoHelperFactory {

	private static Logger log = LoggerFactory.getLogger(DaoHelperFactory.class);

	@Value("${dbURL}")
	private String jdbcURL;
	private GenericDaoHelper daoHelper;
	private DBType dbType;

	@PostConstruct
	private void init() {
		initDaoHelper();
		initDBType();
	}

	private void initDaoHelper() {
		if (daoHelper == null) {
			if (jdbcURL.startsWith("jdbc:db2"))
				daoHelper = new DB2DaoHelper();
			else if (jdbcURL.startsWith("jdbc:mysql"))
				daoHelper = new MySqlDaoHelper();
			else {
				log.error("unsupport dbtype {" + jdbcURL + "}.");
			}
		}
	}

	private void initDBType() {
		dbType = new DBType(jdbcURL);
	}

	public GenericDaoHelper getDaoHelper() {
		return daoHelper;
	}

	public DBType getDBType() {
		return dbType;
	}
}
