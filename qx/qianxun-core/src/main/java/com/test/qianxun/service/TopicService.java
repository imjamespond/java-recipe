package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Topic;

@Service
@Transactional
public class TopicService extends SqlService<Topic, Long>{
	@Autowired
	private ReplyService replyService;
	
	public int countByStype(int stype){
		String sql = "select count(*) from topic where stype = ? and top = 0";
		return sqlTemplate.queryForInt(sql, stype);
	}
	
	public List<Topic> listByStype(int stype, Page page){
		int total = this.countByStype(stype);
		page.setTotal(total);
		String sql = "select * from topic where stype = ? and top = 0 order by replytime desc offset ? limit ?";
		return sqlTemplate.queryForList(sql, Topic.class, stype, page.getOffset(), page.getLimit());
	}
	
	public List<Topic> listTop(int stype, int limit){
		String sql = "select * from topic where stype = ? and top = 1 order by replytime limit ?";
		return sqlTemplate.queryForList(sql, Topic.class, stype, limit);
	}
	
	public void increaseReply(long tid, String username){
		Topic topic = super.get(tid);
		topic.setReply(replyService.countByTid(tid) + 1);
		topic.setLastReply(username);
		topic.setReplyTime(System.currentTimeMillis());
		super.update(topic);
	}
	
	public void increaseRead(long tid){
		Topic topic = super.get(tid);
		topic.setRead(topic.getRead() + 1);
		super.update(topic);
	}
}