package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Charge;

@Deprecated
@Service
@Transactional
public class ChargeService extends SqlService<Charge, Long> {
	public List<Charge> listByUid(long uid, Page page) {
		int total = this.countByUid(uid);
		page.setTotal(total);
		String sql = "select * from charge where uid = ? offset ? limit ?";
		return sqlTemplate.queryForList(sql, Charge.class, uid,
				page.getOffset(), page.getLimit());
	}

	public int countByUid(long uid) {
		String sql = "select count(*) from charge where uid = ?";
		return sqlTemplate.queryForInt(sql, uid);
	}

	public List<Charge> listByTime(long start, long end) {
		String sql = "select * from charge where timeline >= ? and timeline < ?";
		return sqlTemplate.queryForList(sql, Charge.class, start, end);
	}
}