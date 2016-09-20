package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.job.VoteJob;
import com.test.qianxun.model.GameVote;

@Service
@Transactional
public class GameVoteService extends SqlService<GameVote, Long>{
	public List<GameVote> listByRank(int rank,int type){
		String sql = String.format("select gid from game_vote where type = ? order by vote_%d desc", rank);
		return sqlTemplate.queryForList(sql, GameVote.class, type);
	}

	public void addVote(VoteJob vj) {
		String sql = String.format("update game_vote set vote_%d=vote_%d+1 where gid=?", vj.getRank(), vj.getRank());
		sqlTemplate.updateArray(sql,vj.getGid());
	}
	//public void initVote(VoteJob vj) {
		//String sql = "INSERT into game_vote (gid) ( SELECT id FROM game );";
		//String sql = String.format("UPDATE game_vote SET field='C', field2='Z' WHERE id=3;"+
			//"INSERT INTO table (id, field, field2) SELECT 3, 'C', 'Z' WHERE NOT EXISTS (SELECT 1 FROM table WHERE id=3);");
	//}
	public void updateVote(long id, int i, int vote, int trend) {
		String sql = String.format("update game_vote set trend_%d = ?,vote_%d = ? where gid=?", i, i);
		sqlTemplate.updateArray(sql, trend, vote, id);
	}

	public void updateTrend(long id, int i, int count) {
		String sql = String.format("update game_vote set trend_%d = CASE WHEN ? > last_%d THEN -1 WHEN ? < last_%d THEN 1 ELSE 0 END, last_%d=? where gid=?", i, i, i, i);
		sqlTemplate.updateArray(sql, count, count, count, id);
	}
}