package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Board;

@Service
@Transactional
public class BoardService extends SqlService<Board, Long>{
	List<Board> listByFid(long fid){
		String sql = "select * from board where fid = ? order by sort";
		return sqlTemplate.queryForList(sql, Board.class, fid);
	}
}