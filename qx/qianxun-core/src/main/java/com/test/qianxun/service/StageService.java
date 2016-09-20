package com.test.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Stage;

@Service
@Transactional
public class StageService extends SqlService<Stage, Long> {
	public Stage findByUid(long uid) {
		String sql = "select * from stage where uid = ?";
		return sqlTemplate.queryForObject(sql, Stage.class, uid);
	}
}