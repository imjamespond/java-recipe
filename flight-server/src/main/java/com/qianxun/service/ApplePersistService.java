package com.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

import com.qianxun.model.ApplePersist;

@Service
public class ApplePersistService extends SqlService<ApplePersist, Long> {
	//private static final String TABLE = ApplePersist.class.getAnnotation(Table.class).value();

}
