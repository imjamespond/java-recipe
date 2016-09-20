package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Reply;

@Service
@Transactional
public class ReplyService extends SqlService<Reply, Long> {
	public int countByTid(long tid) {
		String sql = "select count(*) from reply where tid = ?";
		return sqlTemplate.queryForInt(sql, tid);
	}

	public List<Reply> listByTid(long tid, Page page) {
		int total = this.countByTid(tid);
		page.setTotal(total);
		String sql = "select * from reply where tid = ? order by id offset ? limit ?";
		return sqlTemplate.queryForList(sql, Reply.class, tid,
				page.getOffset(), page.getLimit());
	}
}