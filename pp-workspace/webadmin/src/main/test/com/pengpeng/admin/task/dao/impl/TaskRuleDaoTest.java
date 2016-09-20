package com.pengpeng.admin.task.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.task.dao.ITaskRuleDao;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class TaskRuleDaoTest implements ITaskRuleDao{
	private static final Log log = LogFactory.getLog(TaskRuleDaoTest.class);

	@Override
	public void createBean(TaskRule taskRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+taskRule);
		}
		try{
			findById(taskRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(TaskRule taskRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+taskRule);
		}
		findById(taskRule.getId());
	}
	
	@Override
	public void removeBean(TaskRule taskRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+taskRule);
		}
		findById(taskRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public TaskRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		TaskRule taskRule = null;
		return taskRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<TaskRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<TaskRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 ITaskRuleDao dao = ctx.getBean(ITaskRuleDao.class);
		 TaskRule taskRule = new TaskRule();
		 dao.createBean(taskRule);
    
    }	
}