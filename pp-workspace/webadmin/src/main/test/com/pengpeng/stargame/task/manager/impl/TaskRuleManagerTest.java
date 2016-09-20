package com.pengpeng.stargame.task.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.task.dao.ITaskRuleDao;
import com.pengpeng.stargame.task.manager.ITaskRuleManager;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class TaskRuleManagerTest implements ITaskRuleManager{
	private static final Log log = LogFactory.getLog(TaskRuleManagerTest.class);
	
	@Autowired
	private ITaskRuleDao taskRuleDao;
	public TaskRule createBean(TaskRule taskRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+taskRule);
		}		
		taskRuleDao.createBean(taskRule);
		return taskRule;
	}

	@Override	
	public void updateBean(TaskRule taskRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+taskRule);
		}
		taskRuleDao.updateBean(taskRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		taskRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(TaskRule taskRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+taskRule);
		}
		taskRuleDao.removeBean(taskRule);
	}
	
	@Override	
	public Page<TaskRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<TaskRule> list = taskRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<TaskRule> page = new Page<TaskRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public TaskRule findById(Serializable id) throws NotFoundBeanException{
		return taskRuleDao.findById(id);
	}
}