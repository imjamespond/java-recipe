package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Rank;

@Service
@Transactional
public class RankService extends SqlService<Rank, Long> {
	public List<Rank> listByGid(long gid) {
		String sql = "select * from rank where gid = ?";
		return sqlTemplate.queryForList(sql, Rank.class, gid);
	}
	
	public List<Rank> listByState(int state){
		String sql = "select * from rank where state = ?";
		return sqlTemplate.queryForList(sql, Rank.class, state);
	}
	
	public List<Rank> listByTypeAndRank(int type, int rank) {
		String sql = "select * from rank where type = ? and rank = ? and state = 1";
		return sqlTemplate.queryForList(sql, Rank.class, type, rank);
	}
	
	public List<Rank> listByVotes(int type, int rank, int limit) {
		String sql = "select * from rank where type = ? and rank = ? and state = 1 order by votes desc,base limit ?";
		return sqlTemplate.queryForList(sql, Rank.class, type, rank, limit);
	}
	
	public Rank findByTypeAndRank(long gid, int type, int rank){
		String sql = "select * from rank where gid = ? and type = ? and rank = ?";
		return sqlTemplate.queryForObject(sql, Rank.class, gid, type, rank);
	}
	
	public List<Rank> listInSection(int type, int rank, int offset, int limit) {
		String sql = "select * from rank where type = ? and rank = ? order by votes desc,base offset ? limit ?";
		return sqlTemplate.queryForList(sql, Rank.class, type, rank, offset, limit);
	}
	
	public int getSort(int type, int rank, int votes, int base){
		String sql = "select * from rank where type = ? and rank = ? and (votes > ? or (votes = ? and base < ?)) order by votes desc,base";
		List<Rank> rankList = sqlTemplate.queryForList(sql, Rank.class, type, rank, votes, votes, base);
		return rankList.size() + 1;
	}

	public void vote(long gid, int type, int rank) {
		Rank r = this.findByTypeAndRank(gid, type, rank);
		int before = this.getSort(type, rank, r.getVotes(), r.getBase());
		r.setVotes(r.getVotes() + 1);
		super.update(r);
		int now = this.getSort(type, rank, r.getVotes(), r.getBase());
		if(now < before){
			r.setRate(1);
			super.update(r);
			int limit = before - now;
			List<Rank> rankList = this.listInSection(type, rank, now, limit);
			for(Rank r1 : rankList){
				r1.setRate(-1);
				super.update(r1);
			}
		}
	}
}