package com.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

import com.qianxun.model.LoggerMatchPersist;

@Service
public class LoggerMatchPersistService extends SqlService<LoggerMatchPersist, Long> {
	//private static final String TABLE = ApplePersist.class.getAnnotation(Table.class).value();

}
