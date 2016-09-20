package com.pengpeng.admin.room.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.room.dao.IRoomItemRuleDao;
import com.pengpeng.admin.room.manager.IRoomItemRuleManager;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class RoomItemRuleManagerTest implements IRoomItemRuleManager{
	private static final Log log = LogFactory.getLog(RoomItemRuleManagerTest.class);
	
	@Autowired
	private IRoomItemRuleDao roomItemRuleDao;
	public RoomItemRule createBean(RoomItemRule roomItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+roomItemRule);
		}		
		roomItemRuleDao.createBean(roomItemRule);
		return roomItemRule;
	}

	@Override	
	public void updateBean(RoomItemRule roomItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+roomItemRule);
		}
		roomItemRuleDao.updateBean(roomItemRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		roomItemRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(RoomItemRule roomItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+roomItemRule);
		}
		roomItemRuleDao.removeBean(roomItemRule);
	}
	
	@Override	
	public Page<RoomItemRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<RoomItemRule> list = roomItemRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<RoomItemRule> page = new Page<RoomItemRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public RoomItemRule findById(Serializable id) throws NotFoundBeanException{
		return roomItemRuleDao.findById(id);
	}
}