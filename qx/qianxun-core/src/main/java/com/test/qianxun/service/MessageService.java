package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Message;

@Service
@Transactional
public class MessageService extends SqlService<Message, Long> {
	public List<Message> listByUid(long uid, Page page) {
		int total = this.countByUid(uid);
		page.setTotal(total);
		String sql = "select * from message where uid = ? order by state ,id offset ? limit ?";
		return sqlTemplate.queryForList(sql, Message.class, uid,
				page.getOffset(), page.getLimit());
	}

	public int countByUid(long uid) {
		String sql = "select count(*) from message where uid = ?";
		return sqlTemplate.queryForInt(sql, uid);
	}
}