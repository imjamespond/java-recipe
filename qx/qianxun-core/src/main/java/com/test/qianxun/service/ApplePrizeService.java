package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.ApplePrize;

@Service
@Transactional
public class ApplePrizeService extends SqlService<ApplePrize, Long> {
	public int countHistory() {
		String sql = "select count(*) from appleprize where state = 1 or state = 2";
		return sqlTemplate.queryForInt(sql);
	}

	public List<ApplePrize> listHistory(Page page) {
		int total = this.countHistory();
		page.setTotal(total);
		String sql = "select * from appleprize where state = 1 or state = 2 offset ? limit ?";
		return sqlTemplate.queryForList(sql, ApplePrize.class,
				page.getOffset(), page.getLimit());
	}

}
