package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Gift;

@Service
@Transactional
public class GiftService extends SqlService<Gift, Long> {
	public List<Gift> listAll() {
		String sql = "select * from gift where state = 1";
		return sqlTemplate.queryForList(sql, Gift.class);
	}
}