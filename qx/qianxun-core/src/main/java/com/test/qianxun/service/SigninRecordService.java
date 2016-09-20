package com.test.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.SigninRecord;

@Service
@Transactional
public class SigninRecordService extends SqlService<SigninRecord, Long> {

	public SigninRecord findByUid(long uid) {
		String sql = "select * from web_signin where uid = ?";
		return sqlTemplate.queryForObject(sql, SigninRecord.class, uid);
	}
}