package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Comment;

@Service
@Transactional
public class CommentService extends SqlService<Comment, Long>{
	public List<Comment> listByRid(long rid){
		String sql = "select * from comment where rid = ?";
		return sqlTemplate.queryForList(sql, Comment.class, rid);
	}
}