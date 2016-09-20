package com.pengpeng.admin.task.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.task.dao.ITaskRuleDao;
import com.pengpeng.admin.task.manager.ITaskRuleManager;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "taskRuleManager")
public class TaskRuleManagerImpl implements ITaskRuleManager{
	private static final Log log = LogFactory.getLog(TaskRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="taskRuleDao")
	private ITaskRuleDao taskRuleDao;

	@Override	
	public TaskRule findById(Serializable id) throws NotFoundBeanException{
		return taskRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<TaskRule> list = taskRuleDao.findPages("from TaskRule a",map,start,pageSize);
        int count = taskRuleDao.count("from TaskRule a");

		Page<TaskRule> page = new Page<TaskRule>();
		page.setPage(pageNo);
		page.setRows(list);
        page.setTotal(count);
		return page;
	}
	
}