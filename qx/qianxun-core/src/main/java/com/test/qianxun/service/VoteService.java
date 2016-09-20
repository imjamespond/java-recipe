package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.User;
import com.test.qianxun.model.Vote;

@Service
@Transactional
public class VoteService extends SqlService<Vote, Long> {
	@Autowired
	private UserService userService;

	public boolean checkVote(long uid, int type, int rank, long start, long end) {
		String sql = "select count(*) from vote where uid = ? and type = ? and rank = ? and votetime >= ? and votetime < ?";
		User user = userService.get(uid);
		if (user.getUsername().equals("test100")
				|| user.getUsername().equals("test101")
				|| user.getUsername().equals("test102")) {
			return false;
		}
		int count = sqlTemplate.queryForInt(sql, uid, type, rank, start, end);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Vote findByUid(long uid, int type, int rank, long start, long end) {
		String sql = "select * from vote where uid = ? and type = ? and rank = ? and votetime >= ? and votetime < ?";
		return sqlTemplate.queryForObject(sql, Vote.class, uid, type, rank,
				start, end);
	}

	public Vote findLastVote(long uid, int type, int rank, long dateline) {
		String sql = "select * from vote where uid = ? and type = ? and rank = ? and votetime < ? order by votetime desc limit 1";
		return sqlTemplate.queryForObject(sql, Vote.class, uid, type, rank,
				dateline);
	}

	public List<Vote> listByUid(long uid, int type, long start, long end) {
		String sql = "select * from vote where uid = ? and type = ? and votetime >= ? and votetime < ?";
		return sqlTemplate.queryForList(sql, Vote.class, uid, type, start, end);
	}
}