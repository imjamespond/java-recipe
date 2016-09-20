package com.test.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.VoteLite;

@Service
@Transactional
public class VoteLiteService extends SqlService<VoteLite, Long> {
	@Autowired
	private UserService userService;


	public boolean checkVote(long uid, int gid, int rank, int week) {
		String sql = String.format("select count(*) from vote_lite where uid = ? and gid = ? and vote_%d= ?",rank);
		int count = sqlTemplate.queryForInt(sql, uid, gid, week);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}