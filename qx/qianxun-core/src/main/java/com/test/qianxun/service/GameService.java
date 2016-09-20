package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Game;

@Service
@Transactional
public class GameService extends SqlService<Game, Long>{
	public int countByType(int type){
		String sql = "select count(*) from game where type = ? and state = 1";
		return sqlTemplate.queryForInt(sql, type);
	}
	
	public int countAll(){
		String sql = "select count(*) from game";
		return sqlTemplate.queryForInt(sql);
	}

	
	public List<Game> listAllByState(int state){
		String sql = "select * from game where state = ?";
		return sqlTemplate.queryForList(sql, Game.class, state);
	}
}