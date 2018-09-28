package com.metasoft.flying.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

public class UserPersistenceService extends SqlService<UserPersist, Long> {
	private static final int[] TYPE_NAME = new int[] {java.sql.Types.VARCHAR};

	public List<UserPersist> getByName(String name) {
		
		Object[] objs = {name};
		return this.sqlTemplate.queryForList("select * from users where name=?", objs);
	}

}

/*
=====POSTGESQL
CREATE SEQUENCE user_id_seq START 101
DROP TABLE IF EXISTS users;
CREATE TABLE users (
id BIGINT NOT NULL ,
username VARCHAR( 128 ) NOT NULL ,
email VARCHAR( 128 ) NOT NULL ,
rose INT NOT NULL ,
germ INT NOT NULL ,
gold INT NOT NULL ,
score INT NOT NULL ,
contribute INT NOT NULL ,
gender INT NOT NULL ,
groupName VARCHAR( 128 ) NOT NULL ,
relationship ??? NOT NULL ,
item ??? NOT NULL ,
fashion ??? NOT NULL ,
PRIMARY KEY ( id ) 
)
*/