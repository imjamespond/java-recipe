package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.UserIpRecord;

@Deprecated
@Service
@Transactional
public class UserIpRecordService extends SqlService<UserIpRecord, Long> {
	public List<UserIpRecord> listByIp(String ip, Page page) {
		int total = this.countByIp(ip);
		page.setTotal(total);
		String sql = "select * from useriprecord where ip = ? order by useriprecord offset ? limit ?";
		return sqlTemplate.queryForList(sql, UserIpRecord.class, ip,
				page.getOffset(), page.getLimit());
	}

	public int countByIp(String ip) {
		String sql = "select count(*) from useriprecord where ip = ?";
		return sqlTemplate.queryForInt(sql, ip);
	}

	public List<UserIpRecord> listByUid(long uid, Page page) {
		int total = this.countByUid(uid);
		page.setTotal(total);
		String sql = "select * from useriprecord where uid = ? order by useriprecord offset ? limit ?";
		return sqlTemplate.queryForList(sql, UserIpRecord.class, uid,
				page.getOffset(), page.getLimit());
	}

	public int countByUid(long uid) {
		String sql = "select count(*) from useriprecord where uid = ?";
		return sqlTemplate.queryForInt(sql, uid);
	}

	public UserIpRecord findByUidAndIp(long uid, String ip) {
		String sql = "select * from useriprecord where uid = ? and ip = ?";
		return sqlTemplate.queryForObject(sql, UserIpRecord.class, uid, ip);
	}
}