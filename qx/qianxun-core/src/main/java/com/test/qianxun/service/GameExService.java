package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.GameEx;

@Service
@Transactional
public class GameExService extends SqlService<GameEx, Long>{
	public int countByType(int type){
		String sql = "select count(*) from game where type = ? and state = 1";
		return sqlTemplate.queryForInt(sql, type);
	}
	
	public int countAll(){
		String sql = "select count(*) from game";
		return sqlTemplate.queryForInt(sql);
	}
	
	public List<GameEx> listByVotes(int type, int rank, int limit){
		String sql = String.format("select a.*,b.trend_%s as rank,b.vote_%s as vote from game as a right join game_vote as b on a.id = b.gid where a.type=? order by b.vote_%d desc limit ?", rank, rank, rank);
		return sqlTemplate.queryForList(sql, GameEx.class,type,limit);
	}
		
	public List<GameEx> listAll(Page page){
		int total = this.countAll();
		page.setTotal(total);
		//String sql = "select * from game order by state,type offset ? limit ?";
		String sql = "select a.*,b.* from game as a left join game_vote as b on a.id = b.gid order by a.state,a.type offset ? limit ?";
		return sqlTemplate.queryForList(sql, GameEx.class, page.getOffset(), page.getLimit());
	}
	
	public List<GameEx> listAllByState(int state){
		String sql = "select * from game where state = ?";
		return sqlTemplate.queryForList(sql, GameEx.class, state);
	}
}