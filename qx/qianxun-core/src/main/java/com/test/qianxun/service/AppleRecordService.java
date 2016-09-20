package com.test.qianxun.service;

import java.util.List;
import java.util.Map;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.AppleRecord;

@Service
@Transactional
public class AppleRecordService extends SqlService<AppleRecord, Long> {
	public int countByTouid(long touid, long start, long end) {
		String sql = "select count(*) from (select fromuid from applerecord where touid = ? and time >= ? and time < ? group by fromuid) as jihe";
		return sqlTemplate.queryForInt(sql, touid, start, end);
	}

	public List<Map<String, Object>> listByTouid(long touid, long start,
			long end, Page page) {
		int total = this.countByTouid(touid, start, end);
		page.setTotal(total);
		String sql = "select fromuid,nickname,sum(number) from applerecord where touid = ? and time >= ? and time < ? group by fromuid,nickname offset ? limit ?";
		return sqlTemplate.queryForList(sql, touid, start, end,
				page.getOffset(), page.getLimit());
	}

	public int sumByTouid(long touid, long start, long end) {
		String sql = "select sum(number) from applerecord where touid = ? and time >= ? and time < ?";
		return sqlTemplate.queryForInt(sql, touid, start, end);
	}
}